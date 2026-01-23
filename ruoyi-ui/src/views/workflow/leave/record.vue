<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="状态" prop="status">
         <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="待审批" value="0" />
            <el-option label="已通过" value="1" />
            <el-option label="已拒绝" value="2" />
         </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="leaveList">
      <el-table-column label="单号" align="center" prop="leaveId" />
      <el-table-column label="申请人" align="center" prop="username" />
      <el-table-column label="天数" align="center" prop="days" />
      <el-table-column label="原因" align="center" prop="reason" />
      <el-table-column label="申请时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag type="info" v-if="scope.row.status == '0'">待审批</el-tag>
          <el-tag type="success" v-else-if="scope.row.status == '1'">已通过</el-tag>
          <el-tag type="danger" v-else>已拒绝</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="经理意见" align="center" prop="auditComment" />
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
// 这里引入请求方法，需要在 api/system/leave.js 里加一个 listLeave
import { listLeave } from "@/api/system/leave";

export default {
  name: "LeaveRecord",
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      leaveList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        status: null,
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
          this.loading = true;
          //  使用封装好的 listLeave 方法
          listLeave(this.queryParams).then(response => {
            this.leaveList = response.rows;
            this.total = response.total;
            this.loading = false;
          });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    }
  }
};
</script>

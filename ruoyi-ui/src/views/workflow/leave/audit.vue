<template>
  <div class="app-container">
    <el-card class="box-card" style="width: 800px; margin: 0 auto;">
      <div slot="header" class="clearfix">
        <span>📋 请假审批详情</span>
      </div>

      <el-form ref="form" :model="form" label-width="100px">
        <el-divider content-position="left">申请信息</el-divider>
        <el-row>
          <el-col :span="12">
            <el-form-item label="申请人：">{{ form.username }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请时间：">{{ form.createTime }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="请假天数：">{{ form.days }} 天</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="请假原因：">{{ form.reason }}</el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">审批处理</el-divider>

                <div v-if="form.status == '0'">
                  <el-form-item label="审批意见">
                    <el-input type="textarea" v-model="auditForm.comment" placeholder="请输入审批意见..." />
                  </el-form-item>
                  <el-form-item>
                    <el-button type="success" icon="el-icon-check" @click="handleAudit('pass')">同 意</el-button>
                    <el-button type="danger" icon="el-icon-close" @click="handleAudit('reject')">拒 绝</el-button>
                    <el-button @click="handleBack">返 回</el-button>
                  </el-form-item>
                </div>

                <div v-else>
                  <el-form-item label="审批结果">
                    <el-tag type="success" v-if="form.status == '1'">已通过</el-tag>
                    <el-tag type="danger" v-else-if="form.status == '2'">已拒绝</el-tag>
                    <el-tag type="info" v-else>未知状态</el-tag>
                  </el-form-item>

                  <el-form-item label="经理意见">
                    <div style="background-color: #f4f4f5; padding: 5px 10px; border-radius: 4px;">
                       {{ form.auditComment ? form.auditComment : '（经理未填写意见）' }}
                    </div>
                  </el-form-item>

                  <el-form-item>
                    <el-button icon="el-icon-back" @click="handleBack">返 回</el-button>
                  </el-form-item>
                </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  data() {
    return {
      // 申请单详情
      form: {},
      // 审批表单
      auditForm: {
        comment: ''
      }
    };
  },
  created() {
    //  核心：从 URL 参数里获取 ID (?id=xxx)
    const leaveId = this.$route.query.id;
    if (leaveId) {
      this.getDetail(leaveId);
    }
  },
  methods: {
    // 获取详情
    getDetail(leaveId) {
      request({
        url: '/system/leave/' + leaveId,
        method: 'get'
      }).then(response => {
        this.form = response.data;
      });
    },
    // 提交审批
    handleAudit(result) {
      const statusText = result === 'pass' ? '同意' : '拒绝';
      this.$modal.confirm('确定要 ' + statusText + ' 该申请吗？').then(() => {
        request({
          url: '/system/leave/audit',
          method: 'post',
          data: {
            leaveId: this.form.leaveId,
            result: result,
            comment: this.auditForm.comment
          }
        }).then(() => {
          this.$modal.msgSuccess("审批成功！");
          this.handleBack(); // 返回上一页
        });
      });
    },
    // 返回
    handleBack() {
      // 关闭当前标签页并返回（若依的特殊写法，或者直接用 router.back）
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push("/message"); // 返回消息台
    }
  }
};
</script>

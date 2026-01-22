<template>
  <div class="app-container">
    <el-card class="box-card" style="width: 600px; margin: 0 auto;">
      <div slot="header" class="clearfix">
        <span>📝 员工请假申请单</span>
      </div>

      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="请假天数">
          <el-input-number v-model="form.days" :min="1" :max="30" label="天数"></el-input-number>
        </el-form-item>

        <el-form-item label="请假原因">
          <el-input type="textarea" v-model="form.reason" placeholder="请输入请假原因..."></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onSubmit" :loading="loading">立即提交</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import request from '@/utils/request' // 使用若依封装的请求工具

export default {
  data() {
    return {
      loading: false,
      form: {
        days: 1,
        reason: ''
      }
    }
  },
  methods: {
    onSubmit() {
      if (!this.form.reason) {
        this.$modal.msgError("请输入请假原因");
        return;
      }

      this.loading = true;
      // 调用我们在第一步写的后端接口
      request({
        url: '/system/leave/submit', // 注意：RuoYi Gateway 会把 /system 转发到 System 模块
        method: 'post',
        data: this.form
      }).then(response => {
        this.$modal.msgSuccess("提交成功！经理已收到通知。");
        this.loading = false;
        this.resetForm();
      }).catch(() => {
        this.loading = false;
      });
    },
    resetForm() {
      this.form = { days: 1, reason: '' };
    }
  }
}
</script>

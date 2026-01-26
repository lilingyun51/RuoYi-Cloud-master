import store from '@/store'
import { Notification } from 'element-ui'
import router from '@/router'
//【新增】引入你的修改接口
import { updateMessage } from "@/api/message/message"

export default {
  ws: null,

  init() {
    const userId = store.getters.id;

    // 如果还没登录（id为空），就不连，防止报错
    if (!userId) {
        console.log('用户未登录，跳过 WebSocket 连接');
        return;
    }

    // 假设端口是 9205
    const wsUrl = `ws://localhost:9205/ws/message/${userId}`;

    if ('WebSocket' in window) {
      this.ws = new WebSocket(wsUrl);

      this.ws.onopen = () => {
        console.log('WebSocket 连接成功，用户ID:', userId);
      };

      this.ws.onmessage = (event) => {
        console.log('📩 收到新消息:', event.data);
        try {
            // 解析消息
            let msg;
            try {
                msg = JSON.parse(event.data);
            } catch (e) {
                msg = { title: '新消息', content: event.data };
            }

            // 1. 接收 Notification 实例
            let notifyInstance = Notification.success({
                title: msg.title || '通知',
                message: msg.content || msg,
                duration: 5000,

                // 【核心修改：点击事件】
                onClick: () => {
                   console.log("弹窗被点击，准备处理...");

                   // A. 优先调用接口标记为“已读”
                   if (msg.messageId) {
                       // 假设 '1' 代表已读，根据你字典调整
                       updateMessage({ messageId: msg.messageId, readStatus: '1' }).then(() => {
                           console.log("消息已标记为已读");
                           // 再次广播事件，让列表刷新状态（变绿）
                           window.dispatchEvent(new CustomEvent('on-message-received'));
                       });
                   }

                   // B. 跳转逻辑
                   if(msg.routerPath) {
                       router.push(msg.routerPath).catch(err => {
                           console.log("已在当前页面");
                       });

                       // 点击后关闭弹窗
                       notifyInstance.close();
                   } else {
                       console.warn("这条消息没有 routerPath，无法跳转");
                       // 如果不需要跳转，也建议关闭弹窗
                       notifyInstance.close();
                   }
                }
            });

            // 收到消息时广播一次（为了刷新列表显示新消息）
            window.dispatchEvent(new CustomEvent('on-message-received'));

        } catch (e) {
            console.error("消息处理失败", e);
        }
      };

      this.ws.onclose = () => {
        console.log('WebSocket 连接关闭');
      };

      this.ws.onerror = (e) => {
          console.log('WebSocket 发生错误', e);
      };
    }
  }
}

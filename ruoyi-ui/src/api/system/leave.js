import request from '@/utils/request'

// 查询请假记录列表
export function listLeave(query) {
  return request({
    url: '/system/leave/list',  // 注意：这里必须和你 LeaveController.java 里的 @RequestMapping 路径一致
    method: 'get',
    params: query
  })
}

// (可选) 查询请假详细
export function getLeave(leaveId) {
  return request({
    url: '/system/leave/' + leaveId,
    method: 'get'
  })
}

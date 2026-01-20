import request from '@/utils/request'

// 查询用户消息列表
export function listMessage(query) {
  return request({
    url: '/message/message/list',
    method: 'get',
    params: query
  })
}

// 查询用户消息详细
export function getMessage(messageId) {
  return request({
    url: '/message/message/' + messageId,
    method: 'get'
  })
}

// 新增用户消息
export function addMessage(data) {
  return request({
    url: '/message/message',
    method: 'post',
    data: data
  })
}

// 修改用户消息
export function updateMessage(data) {
  return request({
    url: '/message/message',
    method: 'put',
    data: data
  })
}

// 删除用户消息
export function delMessage(messageId) {
  return request({
    url: '/message/message/' + messageId,
    method: 'delete'
  })
}

import request from '../utils/request'

export function createOrder(payload) {
  return request.post('/trade/orders', payload)
}

export function fetchOrders(params) {
  return request.get('/trade/orders', { params })
}

export function cancelOrder(orderId, params) {
  return request.delete(`/trade/orders/${orderId}`, { params })
}

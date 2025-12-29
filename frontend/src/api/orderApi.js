import { apiClient } from "./apiClient";

// 1. 获取订单列表
// 对应后端 Controller: GET /orders
// export const getOrders = async () => {
//   try {
//     const response = await apiClient.get("/orders");
//     // 注意：如果你的后端返回格式是 { code: 200, data: [...] }，请改为 return response.data.data;
//     // 目前假设后端直接返回订单数组
//     return response.data;
//   } catch (error) {
//     console.error("Failed to fetch orders:", error);
//     throw error;
//   }
// };
export const getOrders = async () => {
  return new Promise((resolve) => {
    setTimeout(() => {
      console.log("Mock API: Returned fake orders"); // 控制台打印一下，提醒自己现在是假数据
      resolve(MOCK_ORDERS);
    }, 1000); // 延迟 1000 毫秒 (1秒)
  });
};

// 2. 提交新订单
// 对应后端 Controller: POST /orders
// export const createOrder = async (orderData) => {
//   try {
//     const response = await apiClient.post("/orders", orderData);
//     return response.data;
//   } catch (error) {
//     console.error("Failed to create order:", error);
//     throw error;
//   }
// };
export const createOrder = async (orderData) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      console.log("Mock API: Created order with data:", orderData);

      // 我们甚至可以把新下的单“假装”加到列表里 (刷新页面后会重置)
      // 给它生成一个随机 ID
      const newFakeOrder = {
        ...orderData,
        id: Math.random().toString(),
        orderNumber: `ORD-${Date.now()}`,
        status: "pending",
        date: new Date().toISOString().split("T")[0],
      };
      MOCK_ORDERS.unshift(newFakeOrder); // 加到数组最前面

      resolve(newFakeOrder);
    }, 1500); // 假装处理了 1.5秒
  });
};

import axios from "axios";

const BASE_URL = 'http://localhost:8002/api/v1/cart'; 

export const fetchCartByUser = (userId) => 
                              axios.get(`${BASE_URL}/fetch/${userId}`);

export const deleteProdInCart = (userId,productId) => 
                              axios.delete(`${BASE_URL}/delete/${userId}/${productId}`);

export const addProductToCart = (cartItem) => 
                            axios.post(`${BASE_URL}/add`,cartItem);

export const updateCartQuantity = (userId,productId,quantity) => 
                              axios.put(`${BASE_URL}/update/${userId}/${productId}/${quantity}`);

export const removeCartAfterOrder = (userId) => 
                              axios.delete(`${BASE_URL}/deleteCartAfterOrder/${userId}`);


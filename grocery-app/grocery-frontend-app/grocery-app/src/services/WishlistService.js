import axios from 'axios';

const BASE_URL = 'http://localhost:8001/api/v1/wishlist'; 

export const fetchWishlistByUser = (userId) => 
                              axios.get(`${BASE_URL}/fetch/${userId}`);

export const deleteWishlist = (userId,productId) => 
                              axios.delete(`${BASE_URL}/delete/${userId}/${productId}`);

export const addToWishlist = (wishlist) => 
                            axios.post(`${BASE_URL}/add`,wishlist);
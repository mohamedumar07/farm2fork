import axios from "axios";

const BASE_URL = 'http://localhost:8003/api/v1/inventory'; 

export const isProductAvailableInStock = (productId) => 
                          axios.get(`${BASE_URL}/productStockAvailable/${productId}`);

export const getAllInventory = () => 
                        axios.get(`${BASE_URL}/all`);

export const setWarehouseFilter = (location) => 
                        axios.get(`${BASE_URL}/allByWarehouse/${location}`);

export const addInventoryItem = (inventory) => 
                                axios.post(`${BASE_URL}/add`,inventory);      

export const deleteInventoryItem = (id) => 
                                axios.post(`${BASE_URL}/add`,inventory); 
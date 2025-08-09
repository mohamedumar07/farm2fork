import axios from 'axios';

const BASE_URL = 'http://localhost:8000/api/v1/products'; 

export const getAllProducts = () => axios.get(`${BASE_URL}/all`);

export const createProduct = (productSaveDto, imageFile) => {
  const formData = new FormData();
  formData.append('multipartFile', imageFile);
  formData.append('productSaveDto', JSON.stringify(productSaveDto)); 

  return axios.post(`${BASE_URL}/add`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
};

import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createProduct } from '../../services/ProductService';
import '../../css/createproduct.css';

const CreateProduct = () => {
  const [productName, setProductName] = useState('');
  const [productCategory, setProductCategory] = useState('');
  const [productDescription, setProductDescription] = useState('');
  const [price, setPrice] = useState('');
  const [brandName, setBrandName] = useState('');
  const [productImageFile, setProductImageFile] = useState(null);

  const navigate = useNavigate();

  const handleFileChange = (e) => {
    setProductImageFile(e.target.files[0]);
  };

  const saveProducts = async (e) => {
    e.preventDefault();

    if (!productImageFile) {
      alert('Please select an image.');
      return;
    }

    const productDto = {
      productName,
      productCategory,
      productDescription,
      price,
      brandName
    };

    try {
      await createProduct(productDto, productImageFile);
      alert('Product added successfully!');
      navigate('/products');
    } catch (error) {
      console.error('Failed to add product:', error);
      alert('Error adding product');
    }
  };

  return (
    <form onSubmit={saveProducts} style={{ maxWidth: 400, margin: 'auto' }}>
      <h2>Add Product</h2>

      <label>Product Name</label>
      <input type="text" value={productName} onChange={e => setProductName(e.target.value)} required />

      <label>Product Category</label>
      <input type="text" value={productCategory} onChange={e => setProductCategory(e.target.value)} required />

       <label>Product Description</label>
      <input type="text" value={productDescription} onChange={e => setProductDescription(e.target.value)} required />

      <label>Price</label>
      <input type="number" step="0.01" value={price} onChange={e => setPrice(e.target.value)} required />

      <label>Brand Name</label>
      <input type="text" value={brandName} onChange={e => setBrandName(e.target.value)} required />

      <label>Product Image</label>
      <input type="file" accept="image/*" onChange={handleFileChange} required />

      <button type="submit">Submit</button>
    </form>
  );
};

export default CreateProduct;

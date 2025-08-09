import { useState } from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css'
import Header from './components/common/Header';
import Footer from './components/common/Footer';
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import ProductList from './components/product/ProductList';
import CreateProduct from './components/product/CreateProduct';
import WishlistProducts from './components/Wishlist/WishlistProducts';

function App() {

  return (
   <div>
     <BrowserRouter>
        <Header/>
           <Routes>

             <Route path='/' element={<ProductList/>}></Route>

              <Route path='/products' element={<ProductList />}></Route>

              <Route path='/add-product' element={<CreateProduct />}></Route>

              <Route path='/wishlist' element={<WishlistProducts />}></Route>
          
          </Routes>
        <Footer/>
     </BrowserRouter>
   </div>
  )
}

export default App
    
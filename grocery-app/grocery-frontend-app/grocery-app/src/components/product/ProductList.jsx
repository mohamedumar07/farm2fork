import React, { useEffect, useState } from 'react'
import { getAllProducts } from '../../services/ProductService';
import '../../css/productlist.css';
import { useNavigate } from 'react-router-dom';
import { addToWishlist, fetchWishlistByUser } from '../../services/WishlistService';

const ProductList = () => {

    const [products, setProducts] = useState([]);

    const navigator = useNavigate();

    function getAllProductDetails() {
        getAllProducts()
            .then((response) => {
                setProducts(response.data);
            })
            .catch((error) => {
                console.error(error);
            })
    }

    useEffect(() => {
        getAllProductDetails()
    }, []);

    //Adding Product in the Wishlist

    const userId = 1;

    function addWishlistItem(productId){
    const wishlist = {
        userId: 1,
        productId: productId
     };
        addToWishlist(wishlist)
          .then((response) => {
             alert("Product Added to the Wishlist");
             navigator("/");
          })
          .catch((error)=> {
            alert("Error adding the product in the Wishlist");
            console.error(error);
          })
     }

    // useEffect(() => {
    //     setProducts([
    //         {
    //            productName: "Iphone 16 Pro",
    //            productCategory : "Electronics",
    //            price : 67000,
    //            productImageUrl:"/productsassets/iphone16.jfif",
    //            brandName:"Iphone"
    //         },
    //          {
    //            productName: "Boult K40 Headset",
    //            productCategory : "Electronics",
    //            price : 5000,
    //            productImageUrl:"/productsassets/boultk40.jfif",
    //            brandName:"Boult"
    //         },
    //         {
    //            productName: "Mi A65 TV",
    //            productCategory : "Electronics",
    //            price : 45000,
    //            productImageUrl:"/productsassets/miA65Tv.jfif",
    //            brandName:"Redmi"
    //         },
    //         {
    //            productName: "Tomato",
    //            productCategory : "Vegetable",
    //            price : 80,
    //            productImageUrl:"/productsassets/tomato.jfif",
    //            brandName:"GoGreen"
    //         }
    //     ]);
    // }, []);

    function addProducts() {
        navigator("/add-product");
    }

    return (

        <div>
            <div className="product-list">
                <h2 style={{ textAlign: 'center' }}>Product Catalog</h2>
                <button className='btn btn-primary' style={{ marginTop: '20px' }} onClick={addProducts}>Add New Products</button>
                <hr></hr>
                <div className="products-grid">
                    {products.length > 0 ? (
                        products.map((product) => (
                            <div className="product-card" key={product.productId}>
                                <img src={product.productImageUrl} alt={product.productName} />
                                <div className="product-info">
                                    <h3>{product.productName}</h3>
                                    <p><strong>Brand: </strong>{product.brandName}</p>
                                    <p><strong>Category: </strong> {product.productCategory}</p>
                                    <p><strong>Decription: </strong> {product.productDescription}</p>
                                    <p className="price">Rs.{product.price.toFixed(2)}</p>
                                    <div className="product-actions">
                                        <button
                                            className="btn btn-success"
                                        >
                                            Add to Cart
                                        </button>
                                        <button
                                            className="btn btn-outline-danger"
                                            onClick={() => addWishlistItem(product.productId)}

                                        >
                                            Wishlist
                                        </button>
                                    </div>
                                </div>
                            </div>
                        ))

                    ) : (
                        <p>Loading products...</p>
                    )}
                </div>
            </div>
        </div>
    )
}

export default ProductList;
import React, { useEffect, useState } from 'react'
import { getAllProducts } from '../../services/ProductService';
import '../../css/productlist.css';
import { useNavigate } from 'react-router-dom';
import { addToWishlist, fetchWishlistByUser } from '../../services/WishlistService';
import { addProductToCart } from '../../services/CartService';
import 'react-toastify/dist/ReactToastify.css';

const ProductList = () => {

    const [products, setProducts] = useState([]);

    const [quantities, setQuantities] = useState({});

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

    function addWishlistItem(productId) {
        const wishlist = {
            userId: 1,
            productId: productId
        };
        addToWishlist(wishlist)
            .then((response) => {
                alert("Product Added to the Wishlist");
                navigator("/");
            })
            .catch((error) => {
                if (error.response && error.response.status === 409) {
                toast.error("This product is already in the Wishlist");
                alert("This product is already in your wishlist.");
            } else {
                console.error("Failed to add to wishlist", error);
                alert("Failed to add to wishlist. Please try again.");
            }
            })
    }

    //function add to cart
 
    function handleQuantityChange(productId, qty) {
    setQuantities((prev) => ({
        ...prev,
        [productId]: qty,
    }));
   }

function addToCart(productId) {
    const quantity = quantities[productId] || 1;

    const cartItem = {
        userId: 1, // or dynamically from login
        productId: productId,
        quantity: quantity,
    };

    addProductToCart(cartItem)
        .then((response) => {
            alert("Product added to cart!");
        })
        .catch((error) => {
            if (error.response && error.response.status === 409) {
                toast.error("This product is already in your cart.");
                alert("This product is already in your cart.");
            } else {
                console.error("Failed to add to cart", error);
                alert("Failed to add to cart. Please try again.");
            }
        });
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
                                        <input
                                            type="number"
                                            min="1"
                                            defaultValue={1}
                                            onChange={(e) => handleQuantityChange(product.productId, parseInt(e.target.value))}
                                            style={{width:'60px'}}
                                        />
                                        <button
                                            className="btn btn-success"
                                            onClick={() => addToCart(product.productId)}
                                        >
                                            Add To Cart
                                        </button>
                                        <button
                                            className="btn btn-outline-danger"
                                            onClick={() => addWishlistItem(product.productId)}

                                        >
                                            Wish
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
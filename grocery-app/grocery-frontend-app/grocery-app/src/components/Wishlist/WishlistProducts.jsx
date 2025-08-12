import React, { useEffect, useState } from 'react'
import { deleteWishlist, fetchWishlistByUser } from '../../services/WishlistService';
import 'react-toastify/dist/ReactToastify.css';
import { addProductToCart } from '../../services/CartService';
import { isProductAvailableInStock } from '../../services/InventoryService';
import { toast } from 'react-toastify';

const WishlistProducts = () => {

    const [wishlistItems, setWishlistItems] = useState([]);

    const [quantities, setQuantities] = useState({});

    const [wishStockAvailable, setWishStockAvailable] = useState([]);
    const userId = 1;

    function getAllWishlistItemsByUserId() {
        fetchWishlistByUser(userId)
            .then(async (response) => {
                const wishlistProducts = response.data;

                // For each product, check if it's in stock
                const wishStockAvailable = await Promise.all(
                    wishlistProducts.map(async (wishproduct) => {
                        try {
                            const res = await isProductAvailableInStock(wishproduct.productId);
                            return { ...wishproduct, inStock: res.data }; // assuming res.data is true/false
                        } catch (error) {
                            console.error("Inventory check failed for productId", wishproduct.productId);
                            return { ...wishproduct, inStock: false }; // fallback
                        }
                    })
                );

                setWishStockAvailable(wishStockAvailable);
            })
            .catch((error) => {
                console.error("Error fetching Wishlist", error);
            })
    }

    useEffect(() => {
        getAllWishlistItemsByUserId()
    }, []);

    function handleDeleteWishlistProduct(productId) {
        console.log(productId);
        deleteWishlist(userId, productId).then((response) => {
            getAllWishlistItemsByUserId();
        })
            .catch((error) => {
                console.error(error);
            })
    }

    function addToCart(productId) {
        const quantity = quantities[productId] || 1;

        const cartItem = {
            userId: 1,
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
                    alert("This product is already in the cart.");
                } else {
                    console.error("Failed to add to cart", error);
                    alert("Failed to add to cart. Please try again.");
                }
            });
    }

     function handleQuantityChange(productId, qty) {
        setQuantities((prev) => ({
            ...prev,
            [productId]: qty,
        }));
    }

    return (
        <div>
            <div className="product-list">
                <h2 style={{ textAlign: 'center' }}>Wishlist</h2>
                <hr></hr>
                <div className="products-grid">
                    {wishStockAvailable.length > 0 ? (
                        wishStockAvailable.map((wishlistItem) => (
                            <div className="product-card" key={wishlistItem.productId}>
                                <img src={wishlistItem.productPoster} alt={wishlistItem.productName} />
                                <div className="product-info">
                                    <h3>{wishlistItem.productName}</h3>
                                    <p><strong>Brand: </strong>{wishlistItem.brandName}</p>
                                    <p><strong>Category: </strong> {wishlistItem.productCategory}</p>
                                    <p><strong>Decription: </strong> {wishlistItem.productDescription}</p>
                                    <p className="price">Rs.{wishlistItem.price.toFixed(2)}</p>
                                    <div className="product-actions">
                                         {wishlistItem.inStock ? (
                                            <>
                                                <input
                                                    type="number"
                                                    min="1"
                                                    defaultValue={1}
                                                    onChange={(e) =>
                                                        handleQuantityChange(wishlistItem.productId, parseInt(e.target.value))
                                                    }
                                                    style={{ width: '60px' }}
                                                />
                                                <button
                                                    className="btn btn-success"
                                                    onClick={() => addToCart(wishlistItem.productId)}
                                                >
                                                    Add To Cart </button>
                                            </>
                                        ) : (
                                            <span className="btn btn-outline-secondary">Out of Stock</span>
                                        )}

                                        <button
                                            className="btn btn-outline-danger"
                                            onClick={() => handleDeleteWishlistProduct(wishlistItem.productId)}
                                        >
                                            Remove
                                        </button>
                                    </div>
                                </div>
                            </div>
                        ))

                    ) : (
                        <p>Loading Wishlist products...</p>
                    )}
                </div>
            </div>
        </div>
    )
}

export default WishlistProducts
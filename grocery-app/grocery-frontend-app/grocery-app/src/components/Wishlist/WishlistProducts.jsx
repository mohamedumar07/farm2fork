import React, { useEffect,useState } from 'react'
import { deleteWishlist, fetchWishlistByUser } from '../../services/WishlistService';
import 'react-toastify/dist/ReactToastify.css';
import { addProductToCart } from '../../services/CartService';

const WishlistProducts = () => {

  const [wishlistItems, setWishlistItems] = useState([]);
  const userId = 1;

  function getAllWishlistItemsByUserId(){
    fetchWishlistByUser(userId)
      .then((response) => {
        setWishlistItems(response.data);
      })
      .catch((error) => {
        console.error("Error fetching Wishlist",error);
      })
  }

  useEffect(() => {
    getAllWishlistItemsByUserId()
  },[]);

  function handleDeleteWishlistProduct(productId){
     console.log(productId);
     deleteWishlist(userId,productId).then((response) => {
        getAllWishlistItemsByUserId();
     })
     .catch((error) => {
            console.error(error);
     })
  }

  function addToCart(productId) {
      const quantity = 1;
  
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

  return (
     <div>
            <div className="product-list">
                <h2 style={{ textAlign: 'center' }}>Wishlist</h2>
                <hr></hr>
                <div className="products-grid">
                    {wishlistItems.length > 0 ? (
                        wishlistItems.map((wishlistItem) => (
                            <div className="product-card" key={wishlistItem.productId}>
                                <img src={wishlistItem.productPoster} alt={wishlistItem.productName} />
                                <div className="product-info">
                                    <h3>{wishlistItem.productName}</h3>
                                    <p><strong>Brand: </strong>{wishlistItem.brandName}</p>
                                    <p><strong>Category: </strong> {wishlistItem.productCategory}</p>
                                    <p><strong>Decription: </strong> {wishlistItem.productDescription}</p>
                                    <p className="price">Rs.{wishlistItem.price.toFixed(2)}</p>
                                    <div className="product-actions">
                                        <button
                                            className="btn btn-success"
                                            onClick={() => addToCart(wishlistItem.productId)}
                                        >
                                            Add to Cart
                                        </button>
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
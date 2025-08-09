import React, { useEffect,useState } from 'react'
import { deleteWishlist, fetchWishlistByUser } from '../../services/WishlistService';

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
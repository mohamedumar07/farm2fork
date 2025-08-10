import React, { useEffect, useState } from 'react';
import { fetchCartByUser, updateCartQuantity, deleteProdInCart } from '../../services/CartService';
import '../../css/cartproduct.css';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const Cart = () => {
    const userId = 1; // replace with logged in user logic
    const [cartItems, setCartItems] = useState([]);


    function fetchCartItems() {
        fetchCartByUser(userId)
            .then((response) => {
                setCartItems(response.data);
            })
            .catch((error) => {
                console.error("Failed to fetch cart", error);
            });
    }

    useEffect(() => {
        fetchCartItems();
    }, []);

    function handleQuantityChange(productId, newQuantity) {
       updateCartQuantity(userId, productId, newQuantity)
        .then(() => {
            setCartItems((prevItems) =>
                prevItems.map((item) =>
                    item.productId === productId
                        ? {
                              ...item,
                              quantity: newQuantity,
                              totalPrice: (item.totalPrice / item.quantity) * newQuantity,
                          }
                        : item
                )
            );
        })
        .catch((err) => {
            console.error("Failed to update quantity", err);
        });
}


    const totalCartPrice = cartItems.reduce((acc, item) => acc + item.totalPrice, 0);

    function handleDeleteCartProduct(productId){
        console.log(productId);
        deleteProdInCart(userId,productId)
            .then((response) => {
               fetchCartItems();
            })
            .catch((error) => {
                console.error(error);
            })
    }

    function handleRemoveFromCartAterOrder(){
        removeCartAfterOrder(userId)
            .then((response) => {
                console.log("Removed from Cart Successfully after order");
            })
            .catch((error) => {
                console.error(error)
            })
    }

    return (
        <div className="cart-container">
            <h2 style={{ textAlign: 'center' }}>Your Cart</h2>
            <hr />

            <div className="cart-grid">
                {cartItems.map((cartItem) => (
                    <div className="cart-card" key={cartItem.productId}>
                        <img src={cartItem.productPoster} alt={cartItem.productName} />
                        <div className="cart-info">
                            <h3>{cartItem.productName}</h3>
                            <p><strong>Brand:</strong> {cartItem.brandName}</p>
                            <p><strong>Category:</strong> {cartItem.productCategory}</p>
                            <p><strong>Description:</strong> {cartItem.productDescription}</p>

                            <div className="quantity-control">
                                <label>Quantity: </label>
                                <input
                                    type="number"
                                    min="1"
                                    value={cartItem.quantity}
                                    onChange={(e) => handleQuantityChange(cartItem.productId, parseInt(e.target.value))}
                                />
                            </div>

                            <p className="price"><strong>Total:</strong> Rs. {cartItem.totalPrice.toFixed(2)}</p>

                            <div className='cartActions'>
                                <button className="btn btn-outline-danger"
                                      onClick={() => handleDeleteCartProduct(cartItem.productId)}
                                        >
                                            Remove
                                        </button>     
                            </div>
                        </div>
                    </div>
                ))}
            </div>

            <hr />
            <div className="cart-footer">
                <h3>Total Cart Price: Rs. {totalCartPrice.toFixed(2)}</h3>
                <button className="btn btn-success">Place Order</button>
            </div>
        </div>
    );
};

export default Cart;

import {React, useEffect, useState } from 'react'
import { getAllInventory } from '../../services/InventoryService';
import { useNavigate } from 'react-router-dom';

const ProductInventory = () => {

    const [inventoryItems, setInventoryItems] = useState([]);
    const [filteredItems, setFilteredItems] = useState([]);

    const navigator = useNavigate();

    function getAllInventoryDetails() {
        getAllInventory()
            .then((res) => {
                setInventoryItems(res.data);
            })
            .catch((err) => console.error("Failed to load inventory:", err));
    }

    useEffect(() => {
        getAllInventoryDetails()
    }, []);

    function addNewInventory(){
        navigator("/add-inventory");
    }


    return (
        <div className="inventory-page">
            <h2>Inventory Management</h2>

            <table className="table table-bordered">
                <thead>
                    <tr>
                        <th>Product Name</th>
                        <th>Brand</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Warehouse</th>
                        <th>Quantity</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {inventoryItems.map((item) => (
                        <tr key={item.inventoryId}>
                            <td>{item.productName}</td>
                            <td>{item.productBrand}</td>
                            <td>{item.productCategory}</td>
                            <td>{item.price}</td>
                            <td>{item.warehouseLocation}</td>
                            <td>{item.quantity}</td>
                            <td>
                                <button className="btn btn-warning btn-sm" >Edit</button>
                                <button className="btn btn-danger btn-sm" >Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            <button className="btn btn-primary" onClick={addNewInventory}>Add Inventory</button>
        </div>
    )
}

export default ProductInventory
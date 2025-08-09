import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

const Header = () => {

    return (
     <header className="header">
      <div className="header-left">
        <a href="/" className="navbar-brand text-white mb-0">
          Grocery App
        </a>
      </div>

      <div className="header-right">
        <a href="/wishlist" className="btn btn-info text-dark">
          Wishlist
        </a>

        <a href="/login" className="btn btn-info text-dark">
          Login
        </a>
      </div>
    </header>
    );
};

export default Header;
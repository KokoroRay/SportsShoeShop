<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contact Us - Football Shoes Shop</title>
    <link rel="stylesheet" href="styles.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">
</head>
<style>
    /* Reset mặc định */
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

/* Cấu hình cho toàn bộ trang */
body {
    font-family: 'Poppins', sans-serif;
    background-color: #f9f9f9;
    color: #333;
    line-height: 1.6;
}

/* Phần header */
header {
    background-color: #1a1a1a;
    color: #fff;
    padding: 20px;
    text-align: center;
}

header h1 {
    font-size: 2.5rem;
    font-weight: 600;
    margin-bottom: 10px;
}

nav ul {
    list-style: none;
    padding: 0;
    display: flex;
    justify-content: center;
}

nav ul li {
    margin: 0 20px;
}

nav ul li a {
    color: #fff;
    text-decoration: none;
    font-size: 1.1rem;
    padding: 10px;
    transition: color 0.3s ease;
}

nav ul li a:hover {
    color: #ff6347;
}

/* Main section */
main {
    max-width: 1200px;
    margin: 0 auto;
    padding: 40px 20px;
}

/* Phần thông tin liên hệ */
.contact-info {
    background-color: #fff;
    padding: 40px;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    margin-bottom: 40px;
}

.contact-info h2 {
    font-size: 2rem;
    margin-bottom: 20px;
    color: #333;
}

.contact-info p {
    font-size: 1rem;
    margin: 15px 0;
    color: #666;
}

.contact-info .info p {
    font-size: 1.1rem;
    margin-bottom: 10px;
}

/* Form liên hệ */
.contact-form {
    background-color: #fff;
    padding: 40px;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.contact-form h2 {
    font-size: 2rem;
    margin-bottom: 20px;
}

.contact-form form {
    display: flex;
    flex-direction: column;
}

.form-group {
    margin-bottom: 20px;
}

.form-group label {
    font-size: 1.1rem;
    font-weight: 500;
    margin-bottom: 8px;
    display: block;
    color: #333

</style>
<body>
    <header>
        <h1>GROUP 5 STORE</h1>
        <nav>
            <ul>
                <li><a href="home.jsp">Home</a></li>
            </ul>
        </nav>
    </header>

    <main>
        <section class="contact-info">
            <h2>Contact Us</h2>
            <p>If you have any questions or need support, feel free to contact us through any of the following methods:</p>
            
            <div class="info">
                <p><strong>Phone:</strong> +84 123 456 789</p>
                <p><strong>Email:</strong> shopshoegroup5@gmail.com</p>
                <p><strong>Address:</strong> SE1812, FPT University</p>
                <p><strong>Business Hours:</strong> Monday - Saturday, 9:00 AM - 6:00 PM</p>
            </div>
        </section>
    </main>

    <footer>
        <p>&copy; 2024 Football Shoes Shop. All Rights Reserved.</p>
    </footer>
</body>
</html>

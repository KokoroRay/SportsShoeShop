<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Store Rating</title>
        <!-- Bootstrap 5 CDN -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            .star {
                font-size: 2rem;
                cursor: pointer;
                color: #ccc;
            }
            .star.selected, .star:hover, .star:hover ~ .star {
                color: #f39c12;
            }
            #feedback {
                resize: none;
            }
            .hidden {
                display: none;
            }
            .logo {
                width: 200px; /* Image width */
                height: auto; /* Auto height to maintain image ratio */
            }
        </style>
    </head>
    <body>
        <!-- Rating System -->
        <div class="container mt-5">
            <div class="card shadow-sm p-4">
                <a class="navbar-brand" href="home.jsp"><img class="logo" src="asset/logoShop_transparent.png" alt="alt"/></a>
                <h2 class="text-center fw-bold text-uppercase" style="color: red;">Store Rating</h2>
                <p class="text-center text-muted mb-4">We always listen to your feedback to improve our service!</p>

                <div class="rating-container text-center mb-3">
                    <div class="stars d-flex justify-content-center" id="stars">
                        <span class="star" data-value="1">&#9733;</span>
                        <span class="star" data-value="2">&#9733;</span>
                        <span class="star" data-value="3">&#9733;</span>
                        <span class="star" data-value="4">&#9733;</span>
                        <span class="star" data-value="5">&#9733;</span>
                    </div>
                    <p id="rating-value" class="text-muted mt-2">Please select the number of stars you want to rate!</p>
                </div>

                <textarea id="feedback" class="form-control my-3" rows="4" placeholder="Enter your comments..." style="border-color: red;"></textarea>

                <button id="submitBtn" class="btn btn-danger w-100">Submit Rating</button>

                <div id="thankYouMessage" class="text-center hidden mt-3">
                    <h5 class="text-success fw-bold">Thank you for your rating!</h5>
                </div>
            </div>
        </div>

        <!-- Bootstrap 5 JavaScript Bundle CDN -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Script to handle rating
            const stars = document.querySelectorAll('.star');
            const ratingValue = document.getElementById('rating-value');
            const submitBtn = document.getElementById('submitBtn');
            const thankYouMessage = document.getElementById('thankYouMessage');
            let selectedRating = 0;

            stars.forEach(star => {
                star.addEventListener('click', () => {
                    selectedRating = star.getAttribute('data-value');
                    stars.forEach(s => s.classList.remove('selected'));
                    star.classList.add('selected');
                    ratingValue.textContent = `You rated ${selectedRating} stars!`;
                });
            });

            submitBtn.addEventListener('click', () => {
                if (selectedRating > 0) {
                    thankYouMessage.classList.remove('hidden');
                } else {
                    ratingValue.textContent = 'Please select a star rating before submitting your review!';
                }
            });
        </script>
    </body>
</html>

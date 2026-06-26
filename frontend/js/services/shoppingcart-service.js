let cartService;

class ShoppingCartService {

    cart = {
        items: [],
        total: 0
    };

    lastOrderTotal = 0;

    addToCart(productId) {
        const url = `${config.baseUrl}/cart/products/${productId}`;

        axios.post(url, {})
            .then(response => {
                this.setCart(response.data);
                this.updateCartDisplay();
            })
            .catch(error => {
                const data = {
                    error: "Add to cart failed."
                };

                templateBuilder.append("error", data, "errors")
            })
    }

    setCart(data) {
        this.cart = {
            items: [],
            total: 0
        }

        this.cart.total = data.total;

        for (const [key, value] of Object.entries(data.items)) {
            this.cart.items.push(value);
        }
    }

    loadCart() {
        const url = `${config.baseUrl}/cart`;

        axios.get(url)
            .then(response => {
                this.setCart(response.data);
                this.updateCartDisplay();
            })
            .catch(error => {
                const data = {
                    error: "Load cart failed."
                };

                templateBuilder.append("error", data, "errors")
            })
    }

    loadCartPage() {
        const main = document.getElementById("main")
        if (!main) {
            return;
        }

        main.innerHTML = "";

        let div = document.createElement("div");
        div.className = "filter-box";
        main.appendChild(div);

        const contentDiv = document.createElement("div")
        contentDiv.id = "content";
        contentDiv.classList.add("content-form");

        const cartHeader = document.createElement("div")
        cartHeader.classList.add("cart-header")

        const titleGroup = document.createElement("div")
        titleGroup.classList.add("cart-title-group")

        const h1 = document.createElement("h1")
        h1.innerText = "Your cart";
        titleGroup.appendChild(h1);

        const subtitle = document.createElement("p")
        subtitle.classList.add("cart-subtitle")
        subtitle.innerText = this.cart.items.length > 0
            ? "Review your items and complete checkout in a few steps."
            : "Add something you love and come back here whenever you're ready.";
        titleGroup.appendChild(subtitle);

        cartHeader.appendChild(titleGroup);

        const actions = document.createElement("div")
        actions.classList.add("cart-actions")

        const button = document.createElement("button");
        button.classList.add("btn")
        button.classList.add("btn-danger")
        button.innerText = "Clear cart";
        button.addEventListener("click", () => this.clearCart());
        actions.appendChild(button)

        cartHeader.appendChild(actions)
        contentDiv.appendChild(cartHeader)
        main.appendChild(contentDiv);

        if (this.cart.items.length === 0) {
            this.buildEmptyState(contentDiv);
            return;
        }

        const layout = document.createElement("div")
        layout.classList.add("checkout-layout")

        const itemsPanel = document.createElement("div")
        itemsPanel.classList.add("checkout-items-panel")

        this.cart.items.forEach(item => {
            this.buildItem(item, itemsPanel)
        });

        const summaryPanel = document.createElement("aside")
        summaryPanel.classList.add("checkout-summary")

        const subtotal = this.calculateCartSubtotal();
        this.lastOrderTotal = subtotal;

        summaryPanel.innerHTML = `
            <h3>Order summary</h3>
            <div class="summary-row">
                <span>Items</span>
                <strong>${this.cart.items.length}</strong>
            </div>
            <div class="summary-row">
                <span>Subtotal</span>
                <strong>${this.formatCurrency(subtotal)}</strong>
            </div>
            <div class="summary-row">
                <span>Shipping</span>
                <strong>Free</strong>
            </div>
            <div class="summary-row summary-total">
                <span>Total</span>
                <strong>${this.formatCurrency(subtotal)}</strong>
            </div>
        `;

        const checkoutButton = document.createElement("button");
        checkoutButton.classList.add("btn")
        checkoutButton.classList.add("btn-primary")
        checkoutButton.classList.add("checkout-btn")
        checkoutButton.innerText = "Complete checkout";
        checkoutButton.addEventListener("click", () => this.checkout());
        summaryPanel.appendChild(checkoutButton)

        const note = document.createElement("p")
        note.classList.add("checkout-note")
        note.innerText = "Your order will be created with the shipping information already linked to your profile.";
        summaryPanel.appendChild(note)

        layout.appendChild(itemsPanel)
        layout.appendChild(summaryPanel)
        contentDiv.appendChild(layout)
    }

    buildEmptyState(parent) {
        const emptyState = document.createElement("div")
        emptyState.classList.add("checkout-empty")

        const title = document.createElement("h3")
        title.innerText = "Your cart is empty"
        emptyState.appendChild(title)

        const message = document.createElement("p")
        message.innerText = "Pick a few favorites and come back here to finish your order."
        emptyState.appendChild(message)

        const button = document.createElement("button")
        button.classList.add("btn")
        button.classList.add("btn-secondary")
        button.innerText = "Continue shopping"
        button.addEventListener("click", () => loadHome());
        emptyState.appendChild(button)

        parent.appendChild(emptyState)
    }

    buildItem(item, parent) {
        let outerDiv = document.createElement("div");
        outerDiv.classList.add("cart-item");

        let photoDiv = document.createElement("div");
        photoDiv.classList.add("photo")
        let img = document.createElement("img");
        img.src = `/images/products/${item.product.imageUrl}`
        img.alt = item.product.name;
        img.addEventListener("click", () => {
            showImageDetailForm(item.product.name, img.src)
        })
        photoDiv.appendChild(img)
        outerDiv.appendChild(photoDiv);

        let infoDiv = document.createElement("div");
        infoDiv.classList.add("cart-item-info")

        let h4 = document.createElement("h4")
        h4.innerText = item.product.name;
        infoDiv.appendChild(h4);

        let descriptionDiv = document.createElement("div");
        descriptionDiv.classList.add("cart-item-description")
        descriptionDiv.innerText = item.product.description;
        infoDiv.appendChild(descriptionDiv);

        let quantityDiv = document.createElement("div")
        quantityDiv.classList.add("cart-item-quantity")
        quantityDiv.innerText = `Quantity: ${item.quantity}`;
        infoDiv.appendChild(quantityDiv)

        let priceDiv = document.createElement("div")
        priceDiv.classList.add("cart-item-price")
        priceDiv.innerText = this.formatCurrency(item.product.price * item.quantity);
        infoDiv.appendChild(priceDiv)

        outerDiv.appendChild(infoDiv);
        parent.appendChild(outerDiv);
    }

    calculateCartSubtotal() {
        return this.cart.items.reduce((sum, item) => sum + (item.product.price * item.quantity), 0);
    }

    formatCurrency(value) {
        return `$${Number(value || 0).toFixed(2)}`;
    }

    checkout() {
        if (this.cart.items.length === 0) {
            templateBuilder.append("error", {error: "Your cart is empty."}, "errors")
            return;
        }

        const url = `${config.baseUrl}/orders`;

        axios.post(url, {})
            .then(response => {
                const order = response.data;
                this.cart = {
                    items: [],
                    total: 0
                };
                this.updateCartDisplay();
                this.renderCheckoutSuccess(order);
            })
            .catch(error => {
                templateBuilder.append("error", {error: "Checkout failed."}, "errors")
            })
    }

    renderCheckoutSuccess(order) {
        const contentDiv = document.getElementById("content")
        if (!contentDiv) {
            return;
        }

        contentDiv.innerHTML = "";

        const successPanel = document.createElement("div")
        successPanel.classList.add("checkout-success")

        successPanel.innerHTML = `
            <div class="success-badge">✓</div>
            <h2>Order placed successfully</h2>
            <p>Your order has been created and your cart is now empty.</p>
            <div class="success-details">
                <div>
                    <span>Order ID</span>
                    <strong>#${order.orderId || "New order"}</strong>
                </div>
                <div>
                    <span>Total paid</span>
                    <strong>${this.formatCurrency(this.lastOrderTotal)}</strong>
                </div>
                <div>
                    <span>Shipping to</span>
                    <strong>${[order.address, order.city, order.state, order.zip].filter(Boolean).join(", ") || "Your profile address"}</strong>
                </div>
            </div>
        `;

        const actions = document.createElement("div")
        actions.classList.add("checkout-actions")

        const homeButton = document.createElement("button")
        homeButton.classList.add("btn")
        homeButton.classList.add("btn-primary")
        homeButton.innerText = "Continue shopping"
        homeButton.addEventListener("click", () => loadHome());
        actions.appendChild(homeButton)

        const viewCartButton = document.createElement("button")
        viewCartButton.classList.add("btn")
        viewCartButton.classList.add("btn-secondary")
        viewCartButton.innerText = "View cart"
        viewCartButton.addEventListener("click", () => this.loadCartPage());
        actions.appendChild(viewCartButton)

        successPanel.appendChild(actions)
        contentDiv.appendChild(successPanel)
    }

    clearCart() {
        const url = `${config.baseUrl}/cart`;

        axios.delete(url)
            .then(response => {
                this.cart = {
                    items: [],
                    total: 0
                }

                this.cart.total = response.data.total;

                for (const [key, value] of Object.entries(response.data.items)) {
                    this.cart.items.push(value);
                }

                this.updateCartDisplay()
                this.loadCartPage()

            })
            .catch(error => {
                const data = {
                    error: "Empty cart failed."
                };

                templateBuilder.append("error", data, "errors")
            })
    }

    updateCartDisplay() {
        try {
            const itemCount = this.cart.items.length;
            const cartControl = document.getElementById("cart-items")

            cartControl.innerText = itemCount;
        } catch (e) {

        }
    }
}

document.addEventListener('DOMContentLoaded', () => {
    cartService = new ShoppingCartService();

    if (userService.isLoggedIn()) {
        cartService.loadCart();
    }

});
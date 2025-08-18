import { createSlice, PayloadAction } from '@reduxjs/toolkit';

export interface Product {
  id: string;
  postId?: string;
  userId: string;
  user: {
    id: string;
    displayName: string;
    avatar?: string;
  };
  title: string;
  description: string;
  price: number;
  currency: string;
  images: string[];
  category: string;
  size?: string;
  brand?: string;
  condition: 'new' | 'like-new' | 'good' | 'fair';
  isNegotiable: boolean;
  quantity: number;
  shipping: {
    available: boolean;
    cost?: number;
    methods: string[];
  };
  location?: string;
  tags: string[];
  isActive: boolean;
  views: number;
  favorites: number;
  createdAt: string;
  updatedAt: string;
}

export interface CartItem {
  id: string;
  productId: string;
  product: Product;
  quantity: number;
  selectedSize?: string;
  notes?: string;
  addedAt: string;
}

export interface Order {
  id: string;
  buyerId: string;
  sellerId: string;
  items: CartItem[];
  totalAmount: number;
  currency: string;
  status: 'pending' | 'confirmed' | 'shipped' | 'delivered' | 'cancelled';
  shippingAddress: Address;
  paymentMethod: string;
  paymentStatus: 'pending' | 'paid' | 'failed' | 'refunded';
  trackingNumber?: string;
  createdAt: string;
  updatedAt: string;
}

export interface Address {
  id: string;
  name: string;
  street: string;
  city: string;
  state: string;
  zipCode: string;
  country: string;
  phone: string;
  isDefault: boolean;
}

interface EcommerceState {
  // Products
  products: Product[];
  featuredProducts: Product[];
  userProducts: Product[];
  searchResults: Product[];
  currentProduct: Product | null;
  
  // Cart
  cartItems: CartItem[];
  cartTotal: number;
  
  // Orders
  orders: Order[];
  currentOrder: Order | null;
  
  // Favorites
  favoriteProducts: string[];
  
  // Addresses
  addresses: Address[];
  defaultAddress: Address | null;
  
  // UI States
  isLoading: boolean;
  isLoadingProducts: boolean;
  isLoadingCart: boolean;
  isProcessingOrder: boolean;
  
  // Pagination
  productsPage: number;
  hasMoreProducts: boolean;
  
  // Filters
  filters: {
    category?: string;
    minPrice?: number;
    maxPrice?: number;
    condition?: string[];
    size?: string[];
    brand?: string[];
    location?: string;
  };
  
  error: string | null;
}

const initialState: EcommerceState = {
  products: [],
  featuredProducts: [],
  userProducts: [],
  searchResults: [],
  currentProduct: null,
  cartItems: [],
  cartTotal: 0,
  orders: [],
  currentOrder: null,
  favoriteProducts: [],
  addresses: [],
  defaultAddress: null,
  isLoading: false,
  isLoadingProducts: false,
  isLoadingCart: false,
  isProcessingOrder: false,
  productsPage: 0,
  hasMoreProducts: true,
  filters: {},
  error: null,
};

const ecommerceSlice = createSlice({
  name: 'ecommerce',
  initialState,
  reducers: {
    // Products
    setProducts: (state, action: PayloadAction<Product[]>) => {
      state.products = action.payload;
      state.productsPage = 1;
    },
    appendProducts: (state, action: PayloadAction<Product[]>) => {
      state.products = [...state.products, ...action.payload];
      state.productsPage += 1;
      state.hasMoreProducts = action.payload.length > 0;
    },
    setFeaturedProducts: (state, action: PayloadAction<Product[]>) => {
      state.featuredProducts = action.payload;
    },
    setUserProducts: (state, action: PayloadAction<Product[]>) => {
      state.userProducts = action.payload;
    },
    setSearchResults: (state, action: PayloadAction<Product[]>) => {
      state.searchResults = action.payload;
    },
    setCurrentProduct: (state, action: PayloadAction<Product | null>) => {
      state.currentProduct = action.payload;
    },
    updateProduct: (state, action: PayloadAction<Product>) => {
      const updateProductInArray = (products: Product[]) =>
        products.map(product => 
          product.id === action.payload.id ? action.payload : product
        );
      
      state.products = updateProductInArray(state.products);
      state.featuredProducts = updateProductInArray(state.featuredProducts);
      state.userProducts = updateProductInArray(state.userProducts);
      state.searchResults = updateProductInArray(state.searchResults);
    },
    
    // Cart
    addToCart: (state, action: PayloadAction<{product: Product; quantity?: number; selectedSize?: string}>) => {
      const { product, quantity = 1, selectedSize } = action.payload;
      const existingItem = state.cartItems.find(item => 
        item.productId === product.id && item.selectedSize === selectedSize
      );
      
      if (existingItem) {
        existingItem.quantity += quantity;
      } else {
        state.cartItems.push({
          id: Date.now().toString(),
          productId: product.id,
          product,
          quantity,
          selectedSize,
          addedAt: new Date().toISOString(),
        });
      }
      
      state.cartTotal = state.cartItems.reduce((total, item) => 
        total + (item.product.price * item.quantity), 0
      );
    },
    removeFromCart: (state, action: PayloadAction<string>) => {
      state.cartItems = state.cartItems.filter(item => item.id !== action.payload);
      state.cartTotal = state.cartItems.reduce((total, item) => 
        total + (item.product.price * item.quantity), 0
      );
    },
    updateCartItem: (state, action: PayloadAction<{id: string; quantity: number}>) => {
      const { id, quantity } = action.payload;
      const item = state.cartItems.find(item => item.id === id);
      if (item) {
        item.quantity = quantity;
        state.cartTotal = state.cartItems.reduce((total, item) => 
          total + (item.product.price * item.quantity), 0
        );
      }
    },
    clearCart: (state) => {
      state.cartItems = [];
      state.cartTotal = 0;
    },
    
    // Orders
    setOrders: (state, action: PayloadAction<Order[]>) => {
      state.orders = action.payload;
    },
    addOrder: (state, action: PayloadAction<Order>) => {
      state.orders.unshift(action.payload);
    },
    updateOrder: (state, action: PayloadAction<Order>) => {
      state.orders = state.orders.map(order => 
        order.id === action.payload.id ? action.payload : order
      );
    },
    setCurrentOrder: (state, action: PayloadAction<Order | null>) => {
      state.currentOrder = action.payload;
    },
    
    // Favorites
    toggleFavorite: (state, action: PayloadAction<string>) => {
      const productId = action.payload;
      if (state.favoriteProducts.includes(productId)) {
        state.favoriteProducts = state.favoriteProducts.filter(id => id !== productId);
      } else {
        state.favoriteProducts.push(productId);
      }
    },
    setFavoriteProducts: (state, action: PayloadAction<string[]>) => {
      state.favoriteProducts = action.payload;
    },
    
    // Addresses
    setAddresses: (state, action: PayloadAction<Address[]>) => {
      state.addresses = action.payload;
      state.defaultAddress = action.payload.find(addr => addr.isDefault) || null;
    },
    addAddress: (state, action: PayloadAction<Address>) => {
      if (action.payload.isDefault) {
        state.addresses = state.addresses.map(addr => ({ ...addr, isDefault: false }));
        state.defaultAddress = action.payload;
      }
      state.addresses.push(action.payload);
    },
    updateAddress: (state, action: PayloadAction<Address>) => {
      state.addresses = state.addresses.map(addr => 
        addr.id === action.payload.id ? action.payload : addr
      );
      if (action.payload.isDefault) {
        state.defaultAddress = action.payload;
      }
    },
    deleteAddress: (state, action: PayloadAction<string>) => {
      state.addresses = state.addresses.filter(addr => addr.id !== action.payload);
      if (state.defaultAddress?.id === action.payload) {
        state.defaultAddress = state.addresses.find(addr => addr.isDefault) || null;
      }
    },
    
    // Filters
    setFilters: (state, action: PayloadAction<typeof initialState.filters>) => {
      state.filters = action.payload;
    },
    updateFilter: (state, action: PayloadAction<{key: string; value: any}>) => {
      state.filters = { ...state.filters, [action.payload.key]: action.payload.value };
    },
    clearFilters: (state) => {
      state.filters = {};
    },
    
    // Loading states
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoading = action.payload;
    },
    setLoadingProducts: (state, action: PayloadAction<boolean>) => {
      state.isLoadingProducts = action.payload;
    },
    setLoadingCart: (state, action: PayloadAction<boolean>) => {
      state.isLoadingCart = action.payload;
    },
    setProcessingOrder: (state, action: PayloadAction<boolean>) => {
      state.isProcessingOrder = action.payload;
    },
    
    // Error handling
    setError: (state, action: PayloadAction<string | null>) => {
      state.error = action.payload;
    },
    clearError: (state) => {
      state.error = null;
    },
    // Clear all ecommerce data (logout or reset)
    clearEcommerceData: () => initialState,
  },
});

export const {
  setProducts,
  appendProducts,
  setFeaturedProducts,
  setUserProducts,
  setSearchResults,
  setCurrentProduct,
  updateProduct,
  addToCart,
  removeFromCart,
  updateCartItem,
  clearCart,
  setOrders,
  addOrder,
  updateOrder,
  setCurrentOrder,
  toggleFavorite,
  setFavoriteProducts,
  setAddresses,
  addAddress,
  updateAddress,
  deleteAddress,
  setFilters,
  updateFilter,
  clearFilters,
  setLoading,
  setLoadingProducts,
  setLoadingCart,
  setProcessingOrder,
  setError,
  clearError,
  clearEcommerceData,
} = ecommerceSlice.actions;

export default ecommerceSlice.reducer;
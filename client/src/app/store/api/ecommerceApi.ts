import { baseApi } from './baseApi';
import { Product, Order, Address } from '../slices/ecommerceSlice';

export interface CreateProductRequest {
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
  postId?: string;
}

export interface ProductsResponse {
  products: Product[];
  hasMore: boolean;
  totalCount: number;
}

export interface CreateOrderRequest {
  items: Array<{
    productId: string;
    quantity: number;
    selectedSize?: string;
  }>;
  shippingAddressId: string;
  paymentMethod: string;
  notes?: string;
}

export const ecommerceApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    // Products
    getProducts: builder.query<ProductsResponse, {page: number; filters?: any; limit?: number}>({
      query: ({ page, filters, limit = 20 }) => ({
        url: '/store/products',
        params: { page, limit, ...filters },
      }),
      providesTags: ['Product'],
      serializeQueryArgs: ({ endpointName }) => endpointName,
      merge: (currentCache, newItems, { arg }) => {
        if (arg.page === 0) {
          return newItems;
        }
        return {
          ...newItems,
          products: [...currentCache.products, ...newItems.products],
        };
      },
    }),
    getFeaturedProducts: builder.query<Product[], void>({
      query: () => '/store/products/featured',
      providesTags: ['Product'],
    }),
    getUserProducts: builder.query<ProductsResponse, {userId?: string; page: number}>({
      query: ({ userId, page }) => ({
        url: userId ? `/store/products/user/${userId}` : '/store/products/my',
        params: { page },
      }),
      providesTags: ['Product'],
    }),
    getProduct: builder.query<Product, string>({
      query: (productId) => `/store/products/${productId}`,
      providesTags: (result, error, id) => [{ type: 'Product', id }],
    }),
    createProduct: builder.mutation<Product, CreateProductRequest>({
      query: (productData) => ({
        url: '/store/products',
        method: 'POST',
        body: productData,
      }),
      invalidatesTags: ['Product'],
    }),
    updateProduct: builder.mutation<Product, {id: string; updates: Partial<CreateProductRequest>}>({
      query: ({ id, updates }) => ({
        url: `/store/products/${id}`,
        method: 'PATCH',
        body: updates,
      }),
      invalidatesTags: (result, error, { id }) => [{ type: 'Product', id }],
    }),
    deleteProduct: builder.mutation<void, string>({
      query: (productId) => ({
        url: `/store/products/${productId}`,
        method: 'DELETE',
      }),
      invalidatesTags: (result, error, id) => [{ type: 'Product', id }],
    }),
    searchProducts: builder.query<ProductsResponse, {query: string; filters?: any; page: number}>({
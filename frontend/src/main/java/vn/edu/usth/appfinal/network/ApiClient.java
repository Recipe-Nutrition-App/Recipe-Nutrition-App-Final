package vn.edu.usth.appfinal.network;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.collection.LruCache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;


public class ApiClient {
    private static ApiClient instance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static Context ctx;

    /**
     * Khởi tạo Private Constructor: Chỉ được tạo 1 lần duy nhất trong toàn bộ vòng đời ứng dụng.
     */
    private ApiClient(Context context) {
        // Dùng getApplicationContext() để tránh giữ tham chiếu đến Activity khi Activity bị destroy
        ctx = context.getApplicationContext();
        
        // Khởi tạo địa chỉ IP server từ cấu hình lưu trữ
        ApiEndpoints.init(ctx);
        
        // Khởi tạo hàng đợi gửi request mạng của Volley
        requestQueue = getRequestQueue();

        // Khởi tạo ImageLoader kèm bộ nhớ đệm LruCache (lưu tối đa 50 hình ảnh Bitmap gần nhất)
        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap> cache = new LruCache<>(50);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        if (url != null && bitmap != null) {
                            cache.put(url, bitmap);
                        }
                    }
                });
    }

    /**
     * Lấy đối tượng Singleton ApiClient (Đảm bảo Thread-Safe với synchronized)
     */
    public static synchronized ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context);
        }
        return instance;
    }

    /**
     * Hàm tiện ích lấy nhanh RequestQueue để gửi request từ bất kỳ Activity hay Fragment nào
     */
    public static RequestQueue getQueue(Context context) {
        return getInstance(context).getRequestQueue();
    }

    /**
     * Lấy hoặc khởi tạo RequestQueue của Volley
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * Thêm một yêu cầu mạng (JsonObjectRequest, JsonArrayRequest, StringRequest) vào hàng đợi thực thi
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    /**
     * Lấy ImageLoader để nạp ảnh cho NetworkImageView trong RecyclerView
     */
    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}

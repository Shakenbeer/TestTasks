package shakenbeer.com.idttest.imagedownload;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloadService extends IntentService {

    public static final String TAG = ImageDownloadService.class.getSimpleName();
    public static final String IMAGE_DOWNLOAD = "shakenbeer.com.idttest.imagedownload.IMAGE_DOWNLOAD";
    public static final String IMAGE_URL_EXTRA = "shakenbeer.com.idttest.imagedownload.IMAGE_URL_EXTRA";

    public static final String SUCCESS_EXTRA = "shakenbeer.com.idttest.imagedownload.SUCCESS_EXTRA";
    public static final String MESSAGE_EXTRA = "shakenbeer.com.idttest.imagedownload.MESSAGE_EXTRA";
    public static final String SHOULD_CANCEL_EXTRA = "shakenbeer.com.idttest.imagedownload.SHOULD_CANCEL_EXTRA";

    public static final String FILE_NAME = "downloadedImage";


    private volatile boolean isCancelled = false;

    public ImageDownloadService() {
        super(TAG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isCancelled = intent.getBooleanExtra(SHOULD_CANCEL_EXTRA, false);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String imageUrl = intent.getStringExtra(IMAGE_URL_EXTRA);
        boolean thisIsCancelIntent = intent.getBooleanExtra(SHOULD_CANCEL_EXTRA, false);
        if (!thisIsCancelIntent) {
            if (imageUrl == null) {
                sendError("No image url provided.");
            } else {
                downloadImage(imageUrl);
            }
        }
    }

    private void downloadImage(String imageUrl) {
        Bitmap bitmap = null;
        HttpURLConnection connection = null;
        InputStream is = null;
        ByteArrayOutputStream out = null;

        try {
            connection = (HttpURLConnection) new URL(imageUrl).openConnection();
            connection.connect();
            final int length = connection.getContentLength();
            if (length <= 0) {
                sendError("Invalid content length. The URL is probably not pointing to a file.");
                return;
            }
            //instead of BitmapFactory.decodeStream we use next approach
            //in case user cancels downloading
            is = new BufferedInputStream(connection.getInputStream(), 8192);
            out = new ByteArrayOutputStream();
            byte bytes[] = new byte[8192];
            int count;
            while ((count = is.read(bytes)) != -1) {
                if (isCancelled) {
                    sendError("Canceled by user.");
                    return;
                }
                out.write(bytes, 0, count);
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size(), options);

            options.inSampleSize = calculateInSampleSize(options, 1024, 1024);
            options.inJustDecodeBounds = false;

            bitmap = BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size(), options);
            if (bitmap == null) {
                sendError("Downloaded file could not be decoded as bitmap.");
            } else {
                storeFile(bitmap);
            }
        } catch (IOException e) {
            sendError(e.getClass().getSimpleName() + ": " + e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.disconnect();
                if (out != null) {
                    out.flush();
                    out.close();
                }
                if (is != null)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void storeFile(Bitmap bitmap) {
        File file = new File(getFilesDir(), FILE_NAME);

        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                sendError("Unable to remove previous file.");
                return;
            }
        }

        try {
            boolean created = file.createNewFile();
            if (!created) {
                sendError("Unable to create previous new file.");
                return;
            }
        } catch (IOException e) {
            sendError("Error while creating new file: " + e.getMessage());
            return;
        }

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            sendSuccess();
        } catch (FileNotFoundException e) {
            sendError("Error while creating new file: " + e.getMessage());
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendSuccess() {
        Intent intent = new Intent(IMAGE_DOWNLOAD);
        intent.putExtra(SUCCESS_EXTRA, true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendError(String error) {
        Intent intent = new Intent(IMAGE_DOWNLOAD);
        intent.putExtra(SUCCESS_EXTRA, false);
        intent.putExtra(MESSAGE_EXTRA, error);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}

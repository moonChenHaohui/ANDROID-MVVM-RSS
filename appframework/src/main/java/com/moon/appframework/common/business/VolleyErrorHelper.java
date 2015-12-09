package com.moon.appframework.common.business;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.moon.appframework.R;

/**
 * Created by moon on 15/11/7.
 */
public class VolleyErrorHelper  {



/**
 * Returns appropriate message which is to be displayed to the user
 * against the specified error object.
 *
 * @param error
 * @param context
 * @return
 */
public static String getMessage(Object error, Context context) {
    if (error instanceof TimeoutError) {
            return context.getResources().getString(R.string.generic_server_down);
    }
    else if (isServerProblem(error)) {
        return handleServerError(error, context);
    }
    else if (isNetworkProblem(error)) {
        return context.getResources().getString(R.string.no_internet);
    }
    return context.getResources().getString(R.string.generic_error);
}

        /**
         * Determines whether the error is related to network
         * @param error
         * @return
         */
        private static boolean isNetworkProblem(Object error) {
            return (error instanceof NetworkError) || (error instanceof NoConnectionError);
        }
        /**
         * Determines whether the error is related to server
         * @param error
         * @return
         */
        private static boolean isServerProblem(Object error) {
            return (error instanceof ServerError) || (error instanceof AuthFailureError);
        }
        /**
         * Handles the server error, tries to determine whether to show a stock message or to
         * show a message retrieved from the server.
         *
         * @param err
         * @param context
         * @return
         */
        private static String handleServerError(Object err, Context context) {
            VolleyError error = (VolleyError) err;

            NetworkResponse response = error.networkResponse;

            if (response != null) {
                switch (response.statusCode) {
                    case 404:
                    case 422:
                    case 401:
//                        try {
//                            // server might return error like this { "error": "Some error occured" }
//                            // Use "Gson" to parse the result
//                            HashMap<String, String> result = JSON.parseObject(new String(response.data), HashMap.class);
//                            //XLog.e(response.data.toString());
//                            if (result != null && result.containsKey("error")) {
//                                return new String(response.data);
//                            }
//
//                        } catch (Exception e) {
//                            //e.printStackTrace();
//                        }
                        // invalid request
                        return new String(response.data);

                    default:
                        return context.getResources().getString(R.string.generic_server_down);
                }
            }
            return context.getResources().getString(R.string.generic_error);
        }
}

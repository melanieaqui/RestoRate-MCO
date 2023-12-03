package com.mobdeve.s17.aquino.melanie.restorate;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class FirestoreHelper {
    public interface OnQueryCompleteListener {
        void onQueryComplete(boolean found);
    }

    public static class QueryResult {
        private boolean found;

        public boolean isFound() {
            return found;
        }

        public void setFound(boolean found) {
            this.found = found;
        }
    }

    public static void queryForItem(FirebaseFirestore db, String collectionName, String fieldName, String searchValue,QueryResult result,OnQueryCompleteListener listener) {
      //  QueryResult result = new QueryResult();
        new QueryTask(db, collectionName, fieldName, searchValue,result,listener).execute();
    }

    private static class QueryTask extends AsyncTask<Void, Void, Boolean> {
        private final FirebaseFirestore db;
        private final String collectionName;
        private final String fieldName;
        private final String searchValue;
        private final QueryResult result;
        private final OnQueryCompleteListener listener;

        QueryTask(FirebaseFirestore db, String collectionName, String fieldName, String searchValue, QueryResult result, OnQueryCompleteListener listener) {
            this.db = db;
            this.collectionName = collectionName;
            this.fieldName = fieldName;
            this.searchValue = searchValue;
            this.result = result;
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            CollectionReference collectionRef = db.collection(collectionName);

            Query query = collectionRef.orderBy(fieldName)
                    .startAt(searchValue)
                    .endAt(searchValue + "\uf8ff");

            try {
                QuerySnapshot queryDocumentSnapshots = Tasks.await(query.get());
                return !queryDocumentSnapshots.isEmpty();
            } catch (Exception e) {
                Log.e("Firestore", "Error getting documents: " + e.getMessage());
                return false;
            }

        }
        @Override
        protected void onPostExecute(Boolean found) {
            result.setFound(found);
            listener.onQueryComplete(found);
        }
    }
    public interface OnDocumentSnapshotListener {
        void onDocumentSnapshot(DocumentSnapshot documentSnapshot);
    }

    public static void getDocumentByField(FirebaseFirestore db, String collectionName, String fieldName, String searchValue, OnDocumentSnapshotListener listener) {
        CollectionReference collectionRef = db.collection(collectionName);

        Query query = collectionRef.whereEqualTo(fieldName, searchValue).limit(1);

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                // Retrieve the first document matching the query
                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                listener.onDocumentSnapshot(documentSnapshot);
            } else {
                // No document found
                listener.onDocumentSnapshot(null);
            }
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Error getting document by field: " + e.getMessage());
            listener.onDocumentSnapshot(null); // Pass null in case of failure
        });
    }
}

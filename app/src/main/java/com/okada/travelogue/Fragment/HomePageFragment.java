package com.okada.travelogue.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.okada.travelogue.Adapters.RecyclerAdapterAnnouncement;
import com.okada.travelogue.Adapters.RecyclerAdapterCity;
import com.okada.travelogue.Adapters.RecyclerAdapterCompany;
import com.okada.travelogue.HelperClasses.AnnouncementClass;
import com.okada.travelogue.HelperClasses.CityClass;
import com.okada.travelogue.HelperClasses.CompanyClass;
import com.okada.travelogue.R;

import java.util.HashMap;
import java.util.Map;


public class HomePageFragment extends Fragment {
    private RecyclerAdapterCity adapterCity;
    private RecyclerAdapterCompany adapterCompany;
    private RecyclerAdapterAnnouncement adapterAnnouncement;
    private FirebaseFirestore firestore;
    private Map<Long, CityClass> cityClassMap;
    private Map<Long, CompanyClass> companyClassMap;
    private Map<Long, AnnouncementClass> announcementClassMap;
    private View view;
    private RecyclerView cityRecyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_page, container, false);
        fillCityRecycler();
        fillCompanyRecycler();
        fillAnnouncementRecycler();
        return view;
    }

    private void fillCityRecycler() {
        cityClassMap = new HashMap<>();
        firestore = FirebaseFirestore.getInstance();
        cityRecyclerView = view.findViewById(R.id.city_recycler_view);
        adapterCity = new RecyclerAdapterCity(cityClassMap, getContext());
        getCitiesFromFirebase();
        cityRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        cityRecyclerView.setAdapter(adapterCity);
    }

    private void fillCompanyRecycler() {
        companyClassMap = new HashMap<>();
        RecyclerView companyRecyclerView = view.findViewById(R.id.company_recycler_view);
        adapterCompany = new RecyclerAdapterCompany(companyClassMap, getContext());
        getCompaniesFromFirebase();
        companyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        companyRecyclerView.setAdapter(adapterCompany);
    }

    private void fillAnnouncementRecycler() {
        announcementClassMap = new HashMap<>();
        RecyclerView announcementRecyclerView = view.findViewById(R.id.announcement_recycler_view);
        adapterAnnouncement = new RecyclerAdapterAnnouncement(announcementClassMap, getContext());
        getAnnouncementsFromFirebase();
        announcementRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        announcementRecyclerView.setAdapter(adapterAnnouncement);
    }

    private void getCitiesFromFirebase() {
        CollectionReference collectionReference = firestore.collection("Cities");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    System.out.println(error.getLocalizedMessage());
                    Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                } else if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> map = snapshot.getData();
                        String name = (String) map.get("Name");
                        String imageUrl = (String) map.get("ImageURL");
                        String description = (String) map.get("Description");
                        String rating = (String) map.get("Rating");
                        Long id = (Long) map.get("id");
                        cityClassMap.put(id - 1, new CityClass(imageUrl, Float.parseFloat(rating), name, description));
                        adapterCity.notifyDataSetChanged();
                        cityRecyclerView.scrollToPosition(5);
                        cityRecyclerView.suppressLayout(true);
                        cityRecyclerView.suppressLayout(false);
                        cityRecyclerView.scrollToPosition(1);
                        cityRecyclerView.suppressLayout(true);


                    }
                }
            }
        });
    }

    private void getCompaniesFromFirebase() {
        CollectionReference collectionReference1 = firestore.collection("Companies");
        collectionReference1.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                } else if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> map = snapshot.getData();
                        String name = (String) map.get("Name");
                        String imageUrl = (String) map.get("ImageURL");
                        String description = (String) map.get("Description");
                        Long id = (Long) map.get("id");
                        companyClassMap.put(id, new CompanyClass(imageUrl, name, description));
                        adapterCompany.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void getAnnouncementsFromFirebase() {
        CollectionReference collectionReference2 = firestore.collection("Announcements");
        collectionReference2.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                } else if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> map = snapshot.getData();
                        String description = (String) map.get("Title");
                        Long id = (Long) map.get("id");
                        announcementClassMap.put(id, new AnnouncementClass(description));
                        adapterAnnouncement.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
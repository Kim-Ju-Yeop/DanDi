package org.techtown.schooler.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.techtown.schooler.Adapter;
import org.techtown.schooler.DTO;
import org.techtown.schooler.R;
import org.techtown.schooler.SigninUser.SignupActivity;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import retrofit2.Call;
import retrofit2.Callback;

import static androidx.core.content.ContextCompat.getSystemService;
import static org.techtown.schooler.SigninUser.SignupActivity.user;


public class SearchSchoolFragment extends Fragment {

    private Adapter adapter;
    EditText SearchSchoolName;
    private ListView listView;
    EditText decideSchoolName;
    LinearLayout step2;
    Button  Step2Finish;
    EditText Grade;
    EditText Class;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_serachschool, container, false);

        Button button = rootView.findViewById(R.id.Search);
        adapter = new Adapter();

        Step2Finish = rootView.findViewById(R.id.Step2Finish);
        listView = (ListView) rootView.findViewById(R.id.School_ListView);
        step2 = (LinearLayout) rootView.findViewById(R.id.Step2);
        Grade = (EditText)rootView.findViewById(R.id.Grade);
        Class = (EditText)rootView.findViewById(R.id.Class);

        listView.setAdapter(adapter);
        step2.setVisibility(View.INVISIBLE);

        SearchSchoolName = (EditText) rootView.findViewById(R.id.SearchSchoolName);
        decideSchoolName = (EditText) rootView.findViewById(R.id.decideSchoolName);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearch();
            }
        });

        return rootView;
    }

    private void onSearch() {
        String SearchSchool = (SearchSchoolName.getText().toString());
        SearchSchool = SearchSchool.replaceAll(" ", "");

        final Call<Response<Data>> res = NetRetrofit.getInstance().getSignup().SearchSchoolGet(SearchSchool);
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {
                Log.d("Retrofit", response.toString());
                if (response.body() != null) {
                    String[] contents = new String[response.body().getData().getSchoolInfo().size()];
                    String[] titles = new String[response.body().getData().getSchoolInfo().size()];

                    String SchoolName = "";
                    String SchoolLocal = "";

                    if (response.body().getData() != null) {
                        adapter.clear();
                        for (int A = 0; A < response.body().getData().getSchoolInfo().size(); A++) {
                            DTO dto = new DTO();
                            SchoolName = response.body().getData().getSchoolInfo().get(A).getSchool_name();
                            SchoolLocal = response.body().getData().getSchoolInfo().get(A).getSchool_locate();

                            contents[A] = SchoolLocal;
                            titles[A] = SchoolName;

                            dto.setTitle(titles[A]);
                            dto.setContent(contents[A]);

                            adapter.addItem(dto);
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "검색결과가 없습니다", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {
                Log.e("Err", "네트워크 연결오류");
            }
        });

        Step2Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setSchool(decideSchoolName.getText().toString());
                user.setSchool_class(Class.getText().toString());
                user.setSchool_grade(Grade.getText().toString());
                Class.setEnabled(false);
                Grade.setEnabled(false);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick (AdapterView< ? > parent, View view, final int position, long id){
                String SearchSchool = (SearchSchoolName.getText().toString());
                final Call<Response<Data>> res = NetRetrofit.getInstance().getSignup().SearchSchoolGet(SearchSchool);
                res.enqueue(new Callback<Response<Data>>() {
                    @Override
                    public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {
                        Log.d("Retrofit", response.toString());

                        if (response.body().getData() != null) {
                            decideSchoolName.setText(response.body().getData().getSchoolInfo().get(position).getSchool_name());
                            step2.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onFailure(Call<Response<Data>> call, Throwable t) {
                        Log.e("Err", "네트워크 연결오류");
                    }
                });
            }
        });

    }
}

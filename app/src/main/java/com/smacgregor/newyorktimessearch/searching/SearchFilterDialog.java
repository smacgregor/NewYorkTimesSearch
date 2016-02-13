package com.smacgregor.newyorktimessearch.searching;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.smacgregor.newyorktimessearch.R;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFilterDialog.OnSearchFilterFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFilterDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFilterDialog extends DialogFragment {

    private static final String ARGUMENT_SEARCH_FILTER = "ARGUMENT_SEARCH_FILTER";

    @Bind(R.id.checkbox_arts) CheckBox mArtsCheckbox;
    @Bind(R.id.checkbox_fashion) CheckBox mFashion;
    @Bind(R.id.checkbox_sports) CheckBox mSports;
    @Bind(R.id.edit_start_date) EditText mStartDateTextField;
    SearchFilter mSearchFilter;

    private OnSearchFilterFragmentInteractionListener mListener;

    public SearchFilterDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchFilterDialog.
     */
    public static SearchFilterDialog newInstance(SearchFilter searchFilter) {
        SearchFilterDialog fragment = new SearchFilterDialog();
        Bundle args = new Bundle();
        args.putParcelable(ARGUMENT_SEARCH_FILTER, Parcels.wrap(searchFilter));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSearchFilter = Parcels.unwrap(getArguments().getParcelable(ARGUMENT_SEARCH_FILTER));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_filter_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        updateStartDateTextView(mSearchFilter.getStartDate());
        updateNewsDesks();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchFilterFragmentInteractionListener) {
            mListener = (OnSearchFilterFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * Update the dialog fragment with the new date
     * @param date
     */
    public void updateDate(Calendar date) {
        mSearchFilter.setStartDate(date);
        updateStartDateTextView(date);
    }

    @OnCheckedChanged({R.id.checkbox_sports, R.id.checkbox_arts, R.id.checkbox_fashion})
    public void onCheckboxChanged(CompoundButton view, boolean checked) {
        SearchFilter.NewsDesks newsDeskToUpdate = null;
        switch (view.getId()) {
            case R.id.checkbox_sports:
                newsDeskToUpdate = SearchFilter.NewsDesks.SPORTS;
                break;
            case R.id.checkbox_fashion:
                newsDeskToUpdate = SearchFilter.NewsDesks.FASHION_AND_STYLE;
                break;
            case R.id.checkbox_arts:
                newsDeskToUpdate = SearchFilter.NewsDesks.ARTS;
                break;
        }
        mSearchFilter.updateNewsDesk(newsDeskToUpdate, checked);
    }

    @OnClick(R.id.edit_start_date)
    public void onStartDatePickerClicked() {
        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getActivity();
        // The fragments activity will notify us with the new date value
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), listener, 2016, 02, 12);
        datePickerDialog.show();
    }

    @OnClick(R.id.button_save_search_filter)
    public void onSaveSearchFilter() {
        // return input to the activity
        // fortunately our model has already been updated
        mListener.onFinishedSavingSearchFilter(mSearchFilter);
        dismiss();
    }

    /**
     * Set the checked property for our news desks to the correct values
     */
    private void updateNewsDesks() {
        mArtsCheckbox.setChecked(mSearchFilter.isNewsDeskEnabled(SearchFilter.NewsDesks.ARTS));
        mFashion.setChecked(mSearchFilter.isNewsDeskEnabled(SearchFilter.NewsDesks.FASHION_AND_STYLE));
        mSports.setChecked(mSearchFilter.isNewsDeskEnabled(SearchFilter.NewsDesks.SPORTS));
    }

    private void updateStartDateTextView(Calendar date) {
        if (date != null) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            mStartDateTextField.setText(format.format(date.getTime()));
        }
    }

    public interface OnSearchFilterFragmentInteractionListener {
        void onFinishedSavingSearchFilter(SearchFilter searchFilter);
    }
}

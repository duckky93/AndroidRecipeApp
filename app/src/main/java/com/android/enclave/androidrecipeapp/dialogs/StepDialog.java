package com.android.enclave.androidrecipeapp.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.enclave.androidrecipeapp.R;
import com.android.enclave.androidrecipeapp.entities.Recipe;
import com.android.enclave.androidrecipeapp.entities.Step;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class StepDialog extends DialogFragment {

    public static final String STEP = "STEP";
    public static final String RECIPE = "RECIPE";
    public static final String POSITION = "POSITION";

    private StepDialogListener listener;

    @BindView(R.id.edt_step_number)
    EditText edtStepNumber;

    @BindView(R.id.edt_step_description)
    EditText edtStepDescription;

    @BindView(R.id.bt_ok)
    Button btOk;

    private Step step;
    private Recipe recipe;
    private int position;
    private Unbinder unbinder;

    public void setListener(StepDialogListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_step_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        step = (Step) bundle.getSerializable(STEP);
        recipe = (Recipe) bundle.getSerializable(RECIPE);
        position = bundle.getInt(POSITION);
        if (step != null) {
            edtStepNumber.setText(step.getStepName());
            edtStepDescription.setText(step.getStepDescription());
        }
    }

    @OnClick(R.id.bt_ok)
    public void onOKClicked() {
        String name = edtStepNumber.getText().toString().trim();
        String description = edtStepDescription.getText().toString().trim();
        if (!name.isEmpty() && !description.isEmpty()) {
            if (listener != null) {
                if (step == null) {
                    Step step = new Step();
                    step.setStepName(name);
                    step.setStepDescription(description);
                    step.setRecipeId(recipe.getId());
                    listener.onCreateStep(step);
                } else {
                    step.setStepName(name);
                    step.setStepDescription(description);
                    listener.onUpdateStep(step, position);
                }
            }
            edtStepNumber.setText("");
            edtStepDescription.setText("");
            dismiss();
        } else {
            Toast.makeText(getActivity(), getString(R.string.empty_message_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    public interface StepDialogListener {
        void onUpdateStep(Step step, int position);

        void onCreateStep(Step step);
    }
}

package xyz.sonbn.quanlynhansu;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class EditNhanSu extends AppCompatActivity {
    private Button btnChooseImage, btnEditRow, btnBack;
    private ImageView imagePreview;
    private EditText nameEditView, ageEditView, addressEditView, phoneEditView, emailEditView;
    private int idToEdit;
    private AddNhanSu ns;
    private DAOdb daOdb;
    private Bundle dataBundle;
    private boolean allowSave = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nhan_su);
        daOdb = new DAOdb(this);
        ns = new AddNhanSu();

        btnChooseImage = (Button) findViewById(R.id.btnChooseImage);
        btnEditRow = (Button) findViewById(R.id.btnEditRow);
        btnBack = (Button) findViewById(R.id.btnBack);

        nameEditView = (EditText) findViewById(R.id.nameEditText);
        ageEditView = (EditText) findViewById(R.id.ageEditText);
        addressEditView = (EditText) findViewById(R.id.addressEditText);
        phoneEditView = (EditText) findViewById(R.id.phoneEditText);
        emailEditView = (EditText) findViewById(R.id.emailEditText);
        imagePreview = (ImageView) findViewById(R.id.imagePreview);

        Intent data = getIntent();
        dataBundle = data.getBundleExtra("DataToEdit");

        idToEdit = dataBundle.getInt("Id");
        nameEditView.setText(dataBundle.getString("Name"));
        ageEditView.setText(dataBundle.getString("Age"));
        addressEditView.setText(dataBundle.getString("Address"));
        phoneEditView.setText(dataBundle.getString("Phone"));
        emailEditView.setText(dataBundle.getString("Email"));
        imagePreview.setImageBitmap(ImageResizer.decodeSampledBitmapFromFile(dataBundle.getString("Image")));

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(EditNhanSu.this);
                dialog.setContentView(R.layout.custom_dialog_box);
                dialog.setTitle("Alert Dialog View");
                Button btnExit = (Button) dialog.findViewById(R.id.btnExit);
                btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.btnChoosePath).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ns.activeGallery();
                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.btnTakePhoto).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ns.activeTakePhoto();
                        dialog.dismiss();
                    }
                });

                // show dialog on screen
                dialog.show();
            }
        });
        btnEditRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NhanSu nhanSu = new NhanSu();
                if (nameEditView.getText().toString().length() == 0) {
                    nameEditView.setError("Bắt buộc");
                    allowSave = false;
                }

                if (ageEditView.getText().toString().length() == 0) {
                    ageEditView.setError("");
                    allowSave = false;
                }

                if (phoneEditView.getText().toString().length() > 10) {
                    phoneEditView.setError("");
                    allowSave = false;
                }

                if (!ns.isValidEmail(emailEditView.getText().toString())) {
                    emailEditView.setError("Sai định dạng email");
                    allowSave = false;
                }

                if (!(nameEditView.getText().toString().length() == 0) &&
                        !(ageEditView.getText().toString().length() == 0) &&
                        !(phoneEditView.getText().toString().length() > 10) &&
                        (ns.isValidEmail(emailEditView.getText().toString()))) {
                    allowSave = true;
                }

                if (allowSave){
                    nhanSu.setId(idToEdit);
                    nhanSu.setName(nameEditView.getText().toString());
                    nhanSu.setAge(ageEditView.getText().toString());
                    nhanSu.setAddress(addressEditView.getText().toString());
                    nhanSu.setPhone(phoneEditView.getText().toString());
                    nhanSu.setEmail(emailEditView.getText().toString());
                    if (ns.getImagePath() == null){
                        nhanSu.setImage(dataBundle.getString("Image"));
                    } else {
                        nhanSu.setImage(ns.getImagePath());
                    }

                    daOdb.updateRow(nhanSu);
                    finish();
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditNhanSu.this, MainActivity.class));
                finish();
            }
        });
    }
}

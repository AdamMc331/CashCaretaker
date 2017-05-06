package com.androidessence.cashcaretaker.fragments;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;

import com.androidessence.cashcaretaker.R;
import com.androidessence.cashcaretaker.adapters.CategoryAdapter;
import com.androidessence.cashcaretaker.core.CoreFragment;
import com.androidessence.cashcaretaker.data.CCContract;
import com.androidessence.cashcaretaker.dataTransferObjects.Category;

/**
 * A placeholder fragment containing a simple view.
 */
public class ManageCategoriesFragment extends CoreFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private ListView listView;
    private CategoryAdapter adapter;
    private FloatingActionButton addCategoryButton;

    private static final int CATEGORY_LOADER = 0;

    private ActionMode actionMode;

    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            mode.getMenuInflater().inflate(R.menu.repeating_transaction_context_menu, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete_transaction:
                    // The transaction that was selected is passed as the tag
                    // for the action mode.
                    showDeleteAlertDialog((Category) actionMode.getTag());
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
        }
    };

    public ManageCategoriesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRoot((CoordinatorLayout) inflater.inflate(R.layout.fragment_manage_categories, container, false));

        getElements(getRoot());

        // Setup listview
        adapter = new CategoryAdapter(getActivity());
        listView.setAdapter(adapter);
        //TODO: Add delete later when we have a way to delete categories without breaking any existing transactions.
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Category category = new Category((Cursor) adapter.getItem(position));
//                startActionMode(category);
//                return true;
//            }
//        });

        // Setup fab
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategoryDialog();
            }
        });

        return getRoot();
    }

    @Override
    protected void getElements(View view) {
        listView = (ListView) view.findViewById(R.id.category_list_view);
        addCategoryButton = (FloatingActionButton) view.findViewById(R.id.add_category_fab);
    }

    private void showAddCategoryDialog() {
        final EditText editText = new EditText(getActivity());
        editText.setHint("Category Name");
        editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Add Category")
                .setView(editText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String categoryText = editText.getText().toString();
                        if(TextUtils.isEmpty(categoryText)) {
                            editText.setError("Category must not be blank.");
                        } else {
                            Category category = new Category(categoryText);
                            getActivity().getContentResolver().insert(CCContract.CategoryEntry.CONTENT_URI, category.getContentValues());
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        alertDialog.show();
    }

    private void showDeleteAlertDialog(final Category category){
        new AlertDialog.Builder(getActivity())
                .setTitle("Delete Category")
                .setMessage("Are you sure you want to delete " + category.getDescription() + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: Handle update
                        // Remove
                        getActivity().getContentResolver().delete(
                                CCContract.CategoryEntry.CONTENT_URI,
                                CCContract.CategoryEntry._ID + " = ?",
                                new String[]{String.valueOf(category.getIdentifier())}
                        );
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    private void startActionMode(Category category){
        // Don't fire if action mode is already being used
        if(actionMode == null){
            // Start the CAB using the ActionMode.Callback already defined
            actionMode = ((AppCompatActivity)getActivity()).startSupportActionMode(actionModeCallback);
            // Get name to set as title for action bar
            // Need to subtract one to account for Header position
            actionMode.setTitle(category.getDescription());
            // Get account ID to pass as tag.
            actionMode.setTag(category);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(CATEGORY_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id) {
            case CATEGORY_LOADER:
                return new CursorLoader(
                        getActivity(),
                        CCContract.CategoryEntry.CONTENT_URI,
                        CategoryAdapter.Companion.getCATEGORY_COLUMNS(),
                        CCContract.CategoryEntry.COLUMN_IS_DEFAULT + " = ?",
                        new String[] { "0" },
                        CCContract.CategoryEntry.COLUMN_DESCRIPTION
                );
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch(loader.getId()) {
            case CATEGORY_LOADER:
                adapter.swapCursor(data);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch(loader.getId()) {
            case CATEGORY_LOADER:
                adapter.swapCursor(null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }
}

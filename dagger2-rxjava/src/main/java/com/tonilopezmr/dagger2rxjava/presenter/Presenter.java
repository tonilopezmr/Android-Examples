package com.tonilopezmr.dagger2rxjava.presenter;

/**
 * @author Antonio López.
 */
public interface Presenter <T extends Presenter.View>{

    void init();
    void setView(T view);

    void onDestroy();

    interface View {
        void showLoading();
        void hideLoading();
    }
}

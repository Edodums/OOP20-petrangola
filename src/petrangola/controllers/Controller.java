package petrangola.controllers;

import petrangola.models.ObservableModel;
import petrangola.views.View;

public interface Controller {
/*  *//**
   *
   * @param models - array of models that is needed for its behaviour
   *//*
  void setModels(ObservableModel... models);
  
  *//**
   *
   * @param view - associated view
   *//*
  void setViews(View... view);*/
  
  void init();
}

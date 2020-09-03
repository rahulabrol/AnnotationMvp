# AnnotationMvp

AnnotationMvp provides you a way to create MVP architecture for your application.In this application there is no need to create multiple interface for your Model, View & Presenter.
AnnotationMvp will automatically create all those interfaces for you.

# How to use AnnotationMvp

AnnotationMvp is an annotation based library.To use this library you need to know about @Model ,@Presenter and @View.

Add the annotationMVP module in your gradle folder or include the following code to build.gradle file:

android { 

    defaultConfig {    
    minSdkVersion 15
        targetSdkVersion 26
        ..
        .. 
        ..
         javaCompileOptions {
            annotationProcessorOptions {
                className 'com.annotation.mvp.annotationmvp.process.MyProcessor'
            }
        }
    }
}
dependencies {
    annotationProcessor project(':annotationMVP')
    provided project(':annotationMVP')
 }
 
# @MVP
In-order to segregate your code MVP ask you to create different modules for everything.
# Model

Model is where you keep all your business logic's, whether it's WebService consumption, Database queries or any calculation suitable for your application.

# View

View is what your user will see and interact with.

# Presenter

Presenter will be the intermediater that will communicate with Model to get the work done and updates the View accordingly.

Let us take an example where a developer is having a class **LoginActivity** and want to create all the above using AnnotationMvp, so just follow these steps:

- Create an interface and name it Login and annotate it with @MVP.

- Add all the methods that corresponds to your business logic like: public void login(String email, String password); and annotate it with @Presenter

- Add all the methods that corresponds to your view's like: public void onUserLogin(); and annotate it with @View.

> Here @MVP will create an interface

- LoginModel with the method public void login(String email, String password);

- LoginView with the method public void onUserLogin();

- LoginInteractor with the method public void login(String email, String password, OnLoginInteractedListener listener);

- OnLoginInteractedListener with the method public void onUserLogin();


>The only thing that developer need to do is to manage the mapping of the above created interface:

* Create a class with name **LoginPresenterImp** and implement it with **LoginPresenter** and implement the unimplemented methods in it.

* Create a class with name **LoginInteractorImp** and implement it with **LoginInteractor** and implement the unimplemented methods in it.


**Example Code**

```
@MVP
public interface Login {    
    @Presenter
    void login(String email, String password);
    
    @View
    void onUserLogin();
}
```
```
public class LoginPresenterImpl implements LoginPresenter, OnLoginInteractedListener {
    private LoginView view;
    private LoginInteractor interactor;

    public LoginPresenterImpl(LoginView view) {
        this.view = view;
        interactor = new LoginInteractorImpl();
    }

    @Override
    public void login(String email, String password) {
        interactor.login(email, password, this);
    }

    @Override
    public void onUserLogin() {
        view.onUserLogin();
    }
}
public class LoginInteractorImpl implements LoginInteractor {

    @Override
    public void login(String email, String password, final OnLoginInteractedListener listener) {
        // TODO login process
        // TODO after login process
        listener.onUserLogin();
    }
}
```
```
LoginPresenter presenter = new LoginPresenterImp(this);
presenter.login("abc@example.com", "123456");
```

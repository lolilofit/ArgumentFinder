package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;


//@Configuration
public class JavafxConfig {
  //  @Bean(name = "mainView")
    public View getMainView() throws IOException {
        return loadView("mainView", "/fxml/main.fxml");
    }

    protected View loadView(String beanName, String filepath) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(filepath));
        Parent root = loader.load();
        Object controller = loader.getController();

        return new View(beanName, root, controller);
    }


    public class View {
        private Parent parent;
        private Object controller;
        private String name;

        public View(String name, Parent parent, Object controller) {
            this.name = name;
            this.parent = parent;
            this.controller = controller;
        }
        public Parent getParent() {
            return parent;
        }

        public void setParent(Parent parent) {
            this.parent = parent;
        }

        public Object getController() {
            return controller;
        }

        public String getName() {
            return name;
        }

        public void setController(Object controller) {
            this.controller = controller;
        }
    }
}

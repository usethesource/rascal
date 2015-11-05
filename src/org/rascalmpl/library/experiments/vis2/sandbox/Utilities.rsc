module experiments::vis2::sandbox::Utilities
import Prelude;
import experiments::vis2::sandbox::FigureServer;

public void hide(str id) {style(id, visibility = "hidden");}

public void visible(str id) {style(id, visibility = "visible");}
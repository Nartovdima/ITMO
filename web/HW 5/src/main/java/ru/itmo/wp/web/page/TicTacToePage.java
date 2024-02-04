package ru.itmo.wp.web.page;

import ru.itmo.wp.model.TicTacToe.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class TicTacToePage {

    private void action(HttpServletRequest request, Map<String, Object> view) {
        newGame(request, view);
    }

    private void newGame(HttpServletRequest request, Map<String, Object> view) {
        State state = new State();
        request.getSession().setAttribute("state", state);
        view.put("state", state);
    }

    private void onMove(HttpServletRequest request, Map<String, Object> view) {
        State state = (State) request.getSession().getAttribute("state");

        String[] cell_position = request.getParameter("cell").split(" ");
        int row = Integer.parseInt(cell_position[0]);
        int column = Integer.parseInt(cell_position[1]);

        state.makeMove(new Move(state.getTurn(), row, column));

        view.put("state", state);
    }
}

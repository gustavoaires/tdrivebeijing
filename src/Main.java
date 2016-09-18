import java.util.List;

import br.ufc.data.mining.dao.DayDAO;
import br.ufc.data.mining.model.Day;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DayDAO dao = new DayDAO();
		List<Day> day = dao.getAllByDayAndHour("segunda", "18:00:00", "19:00:00");
		System.out.println(day.size());
	}

}

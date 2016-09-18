package br.ufc.data.mining.reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.stream.Stream;

import br.ufc.data.mining.dao.DriveDAO;
import br.ufc.data.mining.model.Drive;

public class FileReader {

	private DriveDAO dao = new DriveDAO();
	
	public void readFile(String arquivo) {
		String fileName = "../TDriveBeijing/src/br/ufc/data/mining/" + arquivo + ".csv";

		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			stream.forEach(s -> processLine(s));
			System.out.println("Iniciou " + arquivo);
			dao.close();
			System.out.println("Finalizou " + arquivo);
		}  catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void processLine(String line) {
		String[] atributes = line.split(";");
		Drive drive = new Drive();
		drive.setId(Long.parseLong(atributes[0]));
		try {
			drive.setDate(atributes[1]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		drive.setLongitude(Double.parseDouble(atributes[2]));
		drive.setLatitude(Double.parseDouble(atributes[3]));
		
		dao.insert(drive);
	}
}

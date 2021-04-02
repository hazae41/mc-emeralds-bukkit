package hazae41.emeralds;

import java.util.List;
import java.util.Optional;

public class Utils {
  public static <T> Optional<T> get(T[] array, int index) {
    try {
      return Optional.of(array[index]);
    } catch (IndexOutOfBoundsException ex) {
      return Optional.empty();
    }
  }

  public static <T> Optional<T> get(List<T> list, int index) {
    try {
      return Optional.of(list.get(index));
    } catch (IndexOutOfBoundsException ex) {
      return Optional.empty();
    }
  }

  public static Optional<Integer> parse(String text) {
    try {
      return Optional.of(Integer.parseInt(text));
    } catch (NumberFormatException ex) {
      return Optional.empty();
    }
  }
}

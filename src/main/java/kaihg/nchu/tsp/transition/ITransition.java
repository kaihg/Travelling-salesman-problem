package kaihg.nchu.tsp.transition;

public interface ITransition<T> {

    void update(T solution);
    void update(T source, T target);
}

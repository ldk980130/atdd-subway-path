package wooteco.subway.path.service;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.domain.PathGraphAlgorithm;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WeightedMultiAlgorithm implements PathGraphAlgorithm {
    private WeightedMultigraph<Long, DefaultWeightedEdge> graph =
            new WeightedMultigraph(DefaultWeightedEdge.class);

    private GraphPath<Long, DefaultWeightedEdge> createGraphPath(Long from, Long to) {
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(from, to);
    }

    @Override
    public List<Station> getShortestPath(Long from, Long to) {
        GraphPath<Long, DefaultWeightedEdge> path = createGraphPath(from, to);
        return path.getVertexList().stream()
                .map(Station::new)
                .collect(Collectors.toList());
    }

    @Override
    public int getShortestDistance(Long from, Long to) {
        GraphPath<Long, DefaultWeightedEdge> path = createGraphPath(from, to);
        return (int) path.getWeight();
    }

    @Override
    public void add(Long upStation, Long downStation, int distance) {
        graph.addVertex(upStation);
        graph.addVertex(downStation);
        graph.setEdgeWeight(graph.addEdge(upStation, downStation), distance);
    }

    @Override
    public void updatePaths(List<Section> sections) {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        sections.forEach(
                section -> add(
                        section.getUpStation().getId(),
                        section.getDownStation().getId(),
                        section.getDistance()));
    }
}

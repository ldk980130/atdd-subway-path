package wooteco.subway.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.LinesFixture;
import wooteco.subway.domain.PathInfo;
import wooteco.subway.domain.Station;

@SpringBootTest
@Transactional
class PathServiceTest {

	@Autowired
	private PathService pathService;
	@Autowired
	private LinesFixture linesFixture;
	private Map<String, Station> stations;

	@BeforeEach
	void init() {
		stations = linesFixture.initAndReturnStationMap();
	}

	@DisplayName("한 라인에서 조회한다.")
	@Test
	void findPath() {
		// given
		Station source = stations.get("내방역");
		Station target = stations.get("논현역");

		// when
		PathInfo pathInfo = pathService.findPath(source, target, 20);

		// then
		assertThat(pathInfo.getStations())
			.map(Station::getName)
			.containsExactly("내방역", "고속터미널역", "반포역", "논현역");
	}

	@DisplayName("한 라인에서 역방향 경로를 조회한다.")
	@Test
	void findPathReverse() {
		// given
		Station source = stations.get("논현역");
		Station target = stations.get("내방역");

		// when
		List<Station> stations = pathService.findPath(source, target, 20)
				.getStations();

		// then
		assertThat(stations)
			.map(Station::getName)
			.containsExactly("논현역", "반포역", "고속터미널역", "내방역");
	}

	@DisplayName("두 라인이 겹쳤을 때 경로를 조회한다.")
	@Test
	void doubleLine() {
		// given
		Station source = stations.get("신사역");
		Station target = stations.get("논현역");

		// when
		List<Station> stations = pathService.findPath(source, target, 20)
			.getStations();

		// then
		assertThat(stations)
			.map(Station::getName)
			.containsExactly("신사역", "잠원역", "고속터미널역", "반포역", "논현역");
	}

	@DisplayName("두 경로 중 짧은 거리를 선택한다.")
	@Test
	void shortestPath() {
		// given
		Station source = stations.get("내방역");
		Station target = stations.get("사평역");

		// when
		List<Station> stations = pathService.findPath(source, target, 20)
			.getStations();

		// then
		assertThat(stations)
			.map(Station::getName)
			.containsExactly("내방역", "고속터미널역", "서초역", "사평역");
	}

	@DisplayName("700원 추가 요금 노선을 지나 최단 경로의 요금을 계산한다.")
	@Test
	void fare() {
		// given
		Station source = stations.get("신사역");
		Station target = stations.get("논현역");

		// when
		PathInfo pathInfo = pathService.findPath(source, target, 20);

		// then
		System.out.println(pathInfo.getStations());
		assertThat(pathInfo.getFare()).isEqualTo(2650);
	}

	@DisplayName("청소년 할인을 적용한다.")
	@Test
	void discountFareTeen() {
		// given
		Station source = stations.get("신사역");
		Station target = stations.get("논현역");

		// when
		PathInfo pathInfo = pathService.findPath(source, target, 18);

		// then
		System.out.println(pathInfo.getStations());
		assertThat(pathInfo.getFare()).isEqualTo(2190);
	}

	@DisplayName("어린이 할인을 적용한다.")
	@Test
	void discountFareChile() {
		// given
		Station source = stations.get("신사역");
		Station target = stations.get("논현역");

		// when
		PathInfo pathInfo = pathService.findPath(source, target, 12);

		// then
		System.out.println(pathInfo.getStations());
		assertThat(pathInfo.getFare()).isEqualTo(1500);
	}
}
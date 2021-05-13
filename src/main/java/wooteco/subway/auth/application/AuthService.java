package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.domain.LoginMember;
import wooteco.subway.auth.dto.LoginRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

@Service
public class AuthService {

    private MemberDao memberDao;
    private JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(LoginRequest loginRequest) {
        Member member = findMemberByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        String accessToken = jwtTokenProvider.createToken(String.valueOf(member.getId()));
        return new TokenResponse(accessToken);
    }

    private Member findMemberByEmailAndPassword(String email, String password) {
        return memberDao.findByEmailAndPassword(email, password);
    }

    public LoginMember findLoginMemberByToken(String token) {
        Long payload = Long.parseLong(jwtTokenProvider.getPayload(token));
        return new LoginMember(payload);
    }
}

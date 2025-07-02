# Search-Books
도서 이미지에서 ISBN을 자동으로 추출하고, 도서 정보를 저장하는 시스템이다.

## 주요 기능
- 이미지에서 OCR을 통한 ISBN 추출
- ISBN 유효성 검증
- 외부 API를 통한 도서 정보 자동 조회
- 데이터베이스에 도서 정보 저장
- 중복 도서 검사

## 기술 스택
- **Backend**: `Java 17`, `Spring boot 3.4`
- **Database**: `H2 (local)`, `MySQL (prod)`
- **OCR**: `Tesseract 5.x`, `Tess4J 5.8.0`
- **Build Tool**: `Gradle`
- **API**: `Google Books API`

# 개발 계획
- [x] 프로젝트 초기 설정
- [x] OCR 서비스 구현
- [x] ISBN 추출 및 검증
- [ ] 도서 정보 API 연동
- [ ] 데이터베이스 연동
- [ ] REST API 구현
- [ ] 에러 처리 및 로깅
- [ ] 테스트 코드 작성
- [ ] 문서화
# 🧭 ForTripper – SSAFY Pair Practice Project

**ForTripper**는 여행자들을 위한 일정 관리 및 추천 기능을 제공하는 웹 기반 서비스입니다.  
이 프로젝트는 SSAFY 12기 관통 페어 프로젝트로 진행되었으며,  
**정한균**(팀장)과 **이은선**이 2인 1조로 2주간 공동 개발하였습니다.
> ⚠️ 참고: 본 프로젝트는 개발 도중 PC 환경 변경으로 인해 일부 커밋의 작성자 이메일이 다르게 기록되었습니다.  
> GitLab에서 `이상현(@ynd521)`으로 표시된 커밋들도 모두 정한균(본인)의 작업입니다.

## 🛠 사용 기술
- Frontend: Vue3
- Backend: Spring Boot
- DB: MySQL

## 📌 주요 기능
- 여행 일정 생성 및 관리
- 지도 기반 여행지 추천
- 사용자 등록 및 로그인

---

## 🧹 프로젝트 정리 내역 (2025-06-02 기준)

- [x] `.env`, `application.properties` 등 **민감정보 제거 및 Git 이력에서 완전 삭제**
- [x] `.vs`, `.idea`, `node_modules`, `upload/images` 등 **불필요한 폴더 정리 및 `.gitignore` 재구성**
- [x] `gitleaks` 도구를 사용하여 **민감정보 누락 여부 최종 검사 완료**
- [x] `BFG`, `git filter-repo`를 이용해 **Git 이력을 정리한 후 기존 GitHub 레포지토리에 강제 푸시 (`--force`)**

> ✅ 현재 이 레포지토리는 민감정보가 포함되지 않았으며, **공개(Public)로 전환 가능한 상태**입니다.
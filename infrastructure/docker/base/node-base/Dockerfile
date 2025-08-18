FROM node:18-alpine

LABEL maintainer="TheRavedApp Team"

WORKDIR /workspace

RUN addgroup -g 1001 -S appgroup \
  && adduser -u 1001 -S appuser -G appgroup \
  && apk add --no-cache bash curl tini

USER appuser

ENTRYPOINT ["/sbin/tini", "--"]
CMD ["node"]



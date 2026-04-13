ALTER TABLE "NasaDB"."PicOfDay"
ADD COLUMN "search_vector" tsvector
GENERATED ALWAYS AS (
    setweight(to_tsvector('english', coalesce("title", '')),'A')||
    setweight(to_tsvector('english', coalesce("explanation", '')),'B')
    )STORED;


CREATE INDEX IDX_SEARCH_GIN ON "NasaDB"."PicOfDay" USING GIN(search_vector);


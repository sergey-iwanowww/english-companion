create table translations (
    id uuid not null,
    chat_id integer not null,
    source_phrase text not null,
    constraint translations_pk primary key (id),
    constraint chat_id_source_phrase_unique unique (chat_id, source_phrase)
);

create table translation_target_phrases (
    translation_id uuid not null,
    phrase text not null,
    constraint translation_id_fk foreign key (translation_id) references translations on update cascade on delete restrict
);

create table questions (
    id uuid not null,
    translation_id uuid not null,
    status varchar(32) not null,
    created_date timestamp not null,
    first_asked_date timestamp,
    last_asked_date timestamp,
    answered_correctly_date timestamp,
    cancelled_date timestamp,
    answer_attempts_count integer not null default 0,
    constraint questions_pk primary key (id),
    constraint translation_id_fk foreign key (translation_id) references translations on update cascade on delete restrict
);
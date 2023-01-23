comment on table tiny_link_shortener.link_status is 'Description of the link statuses';

comment on column tiny_link_shortener.link_status.id is 'ID';
comment on column tiny_link_shortener.link_status.name is 'Unique status name';
comment on column tiny_link_shortener.link_status.description is 'Status description';


comment on table tiny_link_shortener.link is 'Log of visits';

comment on column tiny_link_shortener.link.id is 'ID';
comment on column tiny_link_shortener.link.created is 'When shortlink was created';
comment on column tiny_link_shortener.link.status is 'Status of the shortlink';
comment on column tiny_link_shortener.link.original_link is 'Link for redirection';
comment on column tiny_link_shortener.link.short_link is 'Shortlink (just after the /)';
comment on column tiny_link_shortener.link.available_from is 'When this link starts working';
comment on column tiny_link_shortener.link.available_to is 'When this link stop working';
comment on column tiny_link_shortener.link.max_visit_count is 'MMaximum of the visits count';


-------


comment on table tiny_link_shortener.visit_status is 'Description of the visit statuses';

comment on column tiny_link_shortener.visit_status.id is 'ID';
comment on column tiny_link_shortener.visit_status.name is 'Unique status name';
comment on column tiny_link_shortener.visit_status.description is 'Status description';


comment on table tiny_link_shortener.visit is 'Log of visits';

comment on column tiny_link_shortener.visit.id is 'ID';
comment on column tiny_link_shortener.visit.link_id is 'ID for shortlink';
comment on column tiny_link_shortener.visit.created is 'When visits was occurred';
comment on column tiny_link_shortener.visit.status is 'Status of the visit';
comment on column tiny_link_shortener.visit.ip_address is 'IP address of client';
comment on column tiny_link_shortener.visit.user_agent is 'User agent of the client';
comment on column tiny_link_shortener.visit.headers is 'All http headers from the client';


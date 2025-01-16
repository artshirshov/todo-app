INSERT INTO public.todo_tag(id, name)
VALUES ('b9c309f6-0da0-4a5e-9fa2-b774a56c17ff', 'Test Tag1');

INSERT INTO public.todo_task(id, title, description, done, created_at, updated_at)
VALUES ('8a561a9a-b7c6-4cca-a4da-82b21ca591a4', 'Test Title1', 'Test Description1', false, '2024-02-03 12:39:04.632000', '2024-02-03 12:39:04.632000');

INSERT INTO public.todo_task_tag(task_id, tag_id)
VALUES ('8a561a9a-b7c6-4cca-a4da-82b21ca591a4', 'b9c309f6-0da0-4a5e-9fa2-b774a56c17ff');

INSERT INTO public.todo_tag(id, name)
VALUES ('bc74481c-5321-49de-ac56-7b6e1c829757', 'Test Tag2');

INSERT INTO public.todo_task(id, title, description, done, created_at, updated_at)
VALUES ('482b1119-ead5-4bf0-99df-6a2d6ff6847c', 'Test Title2', 'Test Description2', false, '2024-03-04 12:39:04.632000', '2024-03-04 12:39:04.632000');

INSERT INTO public.todo_task_tag(task_id, tag_id)
VALUES ('482b1119-ead5-4bf0-99df-6a2d6ff6847c', 'bc74481c-5321-49de-ac56-7b6e1c829757');

INSERT INTO public.todo_task(id, title, description, done, created_at, updated_at)
VALUES ('fa36a8e2-2726-4bc5-8e89-a7e58388fa94', 'Test Title3', 'Test Description3', false, '2024-04-04 12:39:04.632000', '2024-04-04 12:39:04.632000');

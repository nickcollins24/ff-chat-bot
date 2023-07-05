#! /usr/bin/env python
import requests
import sys
import argparse
import csv

def main():
    argParser = argparse.ArgumentParser()
    argParser.add_argument("--group-id", help="GroupMe Group Id")
    argParser.add_argument("--access-token", help="GroupMe Access Token")
    argParser.add_argument("--batch-size", help="Batch size for requests to GroupMe", type=int, default=100)
    args = argParser.parse_args()

    group_id = args.group_id
    access_token = args.access_token
    batch_size = args.batch_size

    write_user_images_to_csv(group_id, access_token, batch_size, "group_me_image_export.csv")


def write_user_images_to_csv(group_id, access_token, limit, filename):
    images = []
    before_id = 999999999999999999
    message_count = limit
    batch = 0

    with open(filename, 'w') as csv_file:
        csvwriter = csv.writer(csv_file)
        csvwriter.writerow(['CreatedAt', 'UserId', 'UserName', 'Text', 'ImageUrl'])

        while message_count == limit:
            print(f"processing batch #{batch}")
            messages = get_messages(group_id, access_token, limit, before_id)
            for m in messages:
                attachments = m["attachments"]
                for a in attachments:
                    if a["type"] == "image" and m["sender_type"] == "user":
                        print(f"{m['created_at']}, {m['sender_id']}, {m['sender_type']}, {m['name']}, {m['text']}, {a['url']}")
                        csvwriter.writerow([m['created_at'], m['sender_id'], m['name'], m['text'], a['url']])
            message_count = len(messages)
            before_id = messages[message_count-1]["id"]
            batch += 1


def get_messages(group_id, access_token, limit, before_id):
    response = requests.get(f"https://api.groupme.com/v3/groups/{group_id}/messages?token={access_token}&limit={limit}&before_id={before_id}")
    return response.json()["response"]["messages"]


if __name__ == "__main__":
    main()